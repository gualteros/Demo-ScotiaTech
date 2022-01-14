#!/bin/bash

usage(){
cat << EOF
    Uso de: $0 opciones o flags

    Este script se encarga de lanzar pruebas DAST sobre archery para una URL proporcionada

    OPTIONS:
       -help   Muestra este mensaje
       -h      Hostname del endpoint de archery a usar para lanzar las pruebas
       -u      Usuario de archery para autenticacion
       -p      Password de archery para autenticacion
       -s    URL a scanear con Archery + OWASP ZAP
EOF
}

archery-auth(){
	#Obtener Token
	echo "[AUTH] - Autenticando con archery"
	TOKEN_DATA=$(echo "{\"username\":\"${USER}\",\"password\":\"${PASSWORD}\"}")
	export TOKEN_JSON=$(curl -s http://${ARCHERY_HOST}/api-token-auth/ -X POST -H "Content-Type: application/json" -d ${TOKEN_DATA})
	export TOKEN=$(echo $TOKEN_JSON | awk -F: {'print $2'} | cut -d '"' -f2)

	if [ $TOKEN ]; then
        echo "[INFO] OK, Get Token"
        archery-create-project
    else
        echo "[ERROR] AUTH - Error al obtener el Token: ${TOKEN}"
        exit 2
    fi
}

archery-create-project(){
	PROJECT_DATA=$(echo "{\"project_name\":\"${PROJECT_NAME}\",\"project_disc\":\"${PROJECT_NAME}\",\"project_start\":\"${FECHA}\",\"project_end\":\"${FECHA}\",\"project_owner\":\"jenkins\"}")
	PROJECT_JSON=$(curl -s http://${ARCHERY_HOST}/api/project/ \
    	-X POST \
    	-H "Content-Type: application/json" \
    	-H "Authorization: JWT ${TOKEN}" \
    	-d ${PROJECT_DATA})
	export PROJECT=$(echo $PROJECT_JSON | awk -F, {'print $2'} | awk -F: {'print $2'} | cut -d '"' -f2)
	echo "[INFO] - Projecto creado en Archery: $PROJECT_NAME - $PROJECT"

	if [ $PROJECT ]; then
        echo "[INFO] - Projecto creado en Archery: $PROJECT_NAME - $PROJECT"
        archery-launch-zap
    else
        echo "[ERROR] CREATE PROJECT - Error al crear el proyecto: ${PROJECT}"
        exit 2
    fi
}


archery-launch-zap(){
  SCAN_DATA=$(echo "{\"scan_url\":\"${SCAN_URL}\",\"project_id\":\"${PROJECT}\",\"scanner\":\"zap_scan\",\"scan_id\":\"${PROJECT}\"}")
	SCAN_JSON=$(curl -s http://${ARCHERY_HOST}/api/webscan/ \
    	-X POST \
    	-H "Content-Type: application/json" \
    	-H "Authorization: JWT ${TOKEN}" \
    	-d ${SCAN_DATA})
	export SCAN=$(echo $SCAN_JSON | awk -F, {'print $2'} | awk -F: {'print $2'} | cut -d '"' -f2)
	echo "[INFO] - OWASP ZAP scan creado en Archery => Projecto: $PROJECT_NAME - $PROJECT, Scan: $SCAN "

    if [ $SCAN ]; then
        echo "[INFO] - OWASP ZAP scan creado en Archery => Projecto: $PROJECT_NAME - $PROJECT, Scan: $SCAN "
        archery-get-scan-results
    else
        echo "[ERROR] LAUNCH ZAP - Error al lanzar el escaner: ${SCAN}"
        exit 2
    fi

}

archery-get-scan-results(){
	echo "[INFO] - Obteniendo resultados del OWASP ZAP scan..."
	SCAN_ID=$(echo "{\"scan_id\":\"${SCAN}\"}")
	SCAN_RES=$(curl -s http://$ARCHERY_HOST/api/webscanresult/ \
    	-X POST \
    	-H "Content-Type: application/json" \
    	-H "Authorization: JWT ${TOKEN}" \
    	-d ${SCAN_ID})

	start_time="$(date -u +%s)"

	while [[ true ]]; do
		SCAN_RES=$(curl -s http://$ARCHERY_HOST/api/webscanresult/ \
    	-X POST \
    	-H "Content-Type: application/json" \
    	-H "Authorization: JWT ${TOKEN}" \
    	-d ${SCAN_ID})
		if [[ $SCAN_RES == '[]' ]]; then
			echo "[INFO] - Esperando resultado del scan para OWASP ZAP..."
			sleep 10
		else

		    end_time="$(date -u +%s)"
            elapsed="$(($end_time-$start_time))"
            echo "Total of $elapsed seconds elapsed for process get scan result"

			LOW=$(echo $SCAN_RES | grep -o Low | wc -l)
        	MEDIUM=$(echo $SCAN_RES | grep -o Medium | wc -l)
        	HIGH=$(echo $SCAN_RES | grep -o High | wc -l)
        	echo "[INFO] - Numero de vulnerabilidades LOW: $LOW"
        	echo "[INFO] - Numero de vulnerabilidades MEDIUM: $MEDIUM"
        	echo "[INFO] - Numero de vulnerabilidades HIGH: $HIGH"
        	if [[ $HIGH -gt 0 || $MEDIUM -gt 0 ]]; then
            	echo "[ERROR] - Se han encontrado vulnerabilidades HIGH y/o Medium para la URL"
            	exit 2
        	elif [[ $LOW -gt 10 ]]; then
            	echo "[ERROR] - Se han encontrado mas de 10 vulnerabilidades LOW para la URL"
            	exit 3
        	else
            	echo "[DAST-EXITOSO] - Las pruebas DAST son exitosas para la URL"
            	exit 0
        	fi
		fi
	done
}


##################
#   Main Code    #
##################
#Variables

#ARCHERY_HOST=35.235.101.68
#SCAN_URL=http://example.com
FECHA=`date +"%Y-%m-%d"`

#Obtener nombre del proyecto a crear
PROJECT_NAME=$BUILD_TAG

while getopts :h:u:p:s:help: OPTION
do
     case $OPTION in
         help)
             usage
             exit 1
             ;;
         h)
             ARCHERY_HOST=""
             ARCHERY_HOST=$OPTARG
             ;;
         u)
             USER=""
             USER=$OPTARG
             ;;
         p)
             PASSWORD=""
             PASSWORD=$OPTARG
             ;;
         s)
             SCAN_URL=""
             SCAN_URL=$OPTARG
             ;;
         ?)
             usage
             exit
             ;;
         \?)
            echo "Opcion o flag invalido: -$OPTARG" >&2
            exit 2
            ;;
          :)
            echo "Opcion o flag -$OPTION requiere un argumento" >&2
            exit 2
            ;;
     esac
done

if [[ -z $ARCHERY_HOST ]]; then
   echo "El hostname para el endpoint de Archery no esta definido"
   usage
   exit 2
elif [[ -z $SCAN_URL ]]; then
    echo "La URL a scannear no esta definida"
    usage
    exit 2
elif [[ -z $USER ]]; then
    echo "El usuario para autenticacion no esta definido"
    usage
    exit 2
elif [[ -z $PASSWORD ]]; then
    echo "El password para autenticacion no esta definido"
    usage
    exit 2
else
	archery-auth
	# archery-create-project
	# archery-launch-zap
	# archery-get-scan-results
fi