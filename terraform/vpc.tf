resource "aws_vpc" "main_vpc" {
  cidr_block = "172.31.0.0/16"
  enable_dns_support = true
  enable_dns_hostnames = true
  
  tags = {
    Name = "main_vpc"
  }
}

resource "aws_subnet" "main_subnet_1" {
  vpc_id     = aws_vpc.main_vpc.id
  cidr_block = "172.31.16.0/20"
  availability_zone = "eu-west-3c"
  
  tags = {
    Name = "main_subnet_1"
  }
}

resource "aws_subnet" "main_subnet_2" {
  vpc_id            = aws_vpc.main_vpc.id
  cidr_block        = "172.31.32.0/20"
  availability_zone = "eu-west-3b"
  
  tags = {
    Name = "main_subnet_2"
  }
}

resource "aws_internet_gateway" "main_igw" {
  vpc_id = aws_vpc.main_vpc.id
}

resource "aws_route_table" "main_route_table" {
  vpc_id = aws_vpc.main_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.main_igw.id
  }
}

resource "aws_route_table_association" "main_route_table_association_1" {
  subnet_id      = aws_subnet.main_subnet_1.id
  route_table_id = aws_route_table.main_route_table.id
}

resource "aws_route_table_association" "main_route_table_association_2" {
  subnet_id      = aws_subnet.main_subnet_2.id
  route_table_id = aws_route_table.main_route_table.id
}

output "vpc_id" {
  value = aws_vpc.main_vpc.id
}

output "subnet_id_1" {
  value = aws_subnet.main_subnet_1.id
}

output "subnet_id_2" {
  value = aws_subnet.main_subnet_2.id
}