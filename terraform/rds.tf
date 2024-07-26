resource "aws_security_group" "rds_sg" {
  name_prefix = "rds_sg"
  vpc_id      = aws_vpc.main_vpc.id

  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    security_groups = [aws_security_group.web_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "rds_sg"
  }
}

resource "aws_db_subnet_group" "db_subnet_group" {
  name       = "my-db-subnet-group"
  subnet_ids = [
    aws_subnet.main_subnet_1.id,
    aws_subnet.main_subnet_2.id
  ]

  tags = {
    Name = "my_db_subnet_group"
  }
}

resource "aws_db_instance" "postgresql" {
  allocated_storage    = 20
  storage_type         = "gp2"
  engine               = "postgres"
  engine_version       = "13.15"
  instance_class       = "db.t3.micro"
  identifier           = "pfe"
  username             = "postgres"
  password             = "jegojego"
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  db_subnet_group_name = aws_db_subnet_group.db_subnet_group.name
  publicly_accessible  = true
  skip_final_snapshot  = true

  tags = {
    Name = "my_postgresql_instance"
  }
}

output "rds_endpoint" {
  value = aws_db_instance.postgresql.endpoint
}