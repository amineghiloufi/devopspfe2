resource "aws_s3_bucket" "pfe_bucket" {
  bucket = "pfes3bucket"
  acl    = "private"

  tags = {
    Name = "pfe-bucket"
  }
}