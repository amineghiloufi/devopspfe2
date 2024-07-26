resource "aws_instance" "pfe_app" {
  ami           = var.ami_id
  instance_type = var.instance_type
  key_name      = var.key_name
  subnet_id     = aws_subnet.main_subnet_1.id
  vpc_security_group_ids      = [aws_security_group.web_sg.id]
  associate_public_ip_address = true

  tags = {
    Name = "PFEInstance"
  }

  provisioner "remote-exec" {
    inline = [
      "sudo yum update -y",
      "if ! command -v docker &> /dev/null; then sudo amazon-linux-extras install docker -y; fi",
      "if ! sudo systemctl is-active --quiet docker; then sudo service docker start; fi",
      "if ! getent group docker; then sudo groupadd docker; fi",
      "if ! id -nG ec2-user | grep -qw docker; then sudo usermod -a -G docker ec2-user; fi",
      "sudo docker ps -q --filter 'publish=4444' | xargs -r sudo docker rm -f",
      "sudo docker pull jegoniseghiloufi/pfe:latest",
      "if ! command -v psql &> /dev/null; then sudo yum install -y postgresql; fi",
      "PGPASSWORD='jegojego' psql -h pfe.cvio8aq0wi83.eu-west-3.rds.amazonaws.com -U postgres -tc \"SELECT 1 FROM pg_database WHERE datname = 'pfe'\" | grep -q 1 || PGPASSWORD='jegojego' psql -h pfe.cvio8aq0wi83.eu-west-3.rds.amazonaws.com -U postgres -c \"CREATE DATABASE pfe\"",
      "sudo docker run -d -p 4444:4444 jegoniseghiloufi/pfe:latest",
      "sleep 20",
      "container_id=$(sudo docker ps -q --filter ancestor=jegoniseghiloufi/pfe:latest)",
      "sudo docker logs $container_id",
      "scp -i ~/.ssh/ssh_key_pair ~/Desktop/MyFiles/Projects/PFEProject/logstash/logstash.conf ec2-user@${self.public_ip}:/home/ec2-user/logstash.conf",
      "sudo docker pull docker.elastic.co/elasticsearch/elasticsearch:7.9.3",
      "sudo docker pull docker.elastic.co/logstash/logstash:7.9.3",
      "sudo docker pull docker.elastic.co/kibana/kibana:7.9.3",
      "sudo docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 -e \"discovery.type=single-node\" docker.elastic.co/elasticsearch/elasticsearch:7.9.3",
      "sudo docker run -d --name kibana -p 5601:5601 --link elasticsearch:elasticsearch docker.elastic.co/kibana/kibana:7.9.3",
      "sudo docker run -d --name logstash -v /home/ec2-user/logstash.conf:/usr/share/logstash/pipeline/logstash.conf -p 5044:5044 docker.elastic.co/logstash/logstash:7.9.3",
      "sleep 20",
      "echo 'Checking if Elasticsearch is running'",
      "if sudo docker ps | grep elasticsearch; then echo 'Elasticsearch is running'; else echo 'Elasticsearch failed to start'; fi",
      "echo 'Checking if Kibana is running'",
      "if sudo docker ps | grep kibana; then echo 'Kibana is running'; else echo 'Kibana failed to start'; fi"
    ]

    connection {
      type        = "ssh"
      user        = "ec2-user"
      private_key = file("~/.ssh/ssh_key_pair")
      host        = self.public_ip
    }
  }
}

resource "aws_security_group" "web_sg" {
  name_prefix = "web_sg"
  vpc_id      = aws_vpc.main_vpc.id

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "web_sg"
  }
}

output "instance_ip" {
  value = aws_instance.pfe_app.public_ip
}
