cd Terraform/multicloudk8s/
terraform init
terraform validate
terraform apply --auto-approve
cat /home/daniel/Documents/PersonalRepository/Demo-ScotiaTech/ansible-ws/inventory
ssh -i k8s-multi-cloud-key-public ubuntu@3.138.181.196
cd ../../ansible-ws
ansible-inventory --graph
ansible-playbook site.yml
