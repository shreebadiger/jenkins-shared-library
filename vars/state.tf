terraform {
  backend "s3" {
    bucket = "terraform-remote-d76"
    key    = "golden-ami/terraform.tfstate"
    region = "us-east-1"
  }
}