variable "aws_region" {
  description = "The AWS region to deploy in"
  default     = "eu-west-3"
}

variable "key_name" {
  description = "The name of the SSH key pair"
  default     = "deployer-key"
}

variable "instance_type" {
  description = "The type of instance to use"
  default     = "t2.micro"
}

variable "ami_id" {
  description = "The AMI ID to use for the instance"
  default     = "ami-08cb9e86d3adf9004" # Amazon Linux 2 AMI (HVM)
}