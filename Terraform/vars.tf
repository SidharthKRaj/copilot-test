variable "instance_type" {
    description = "The type of EC2 instance to launch"
    type        = string
    default     = "t2.micro"
}

variable "ami_id" {
    description = "The ID of the AMI to use for the EC2 instance"
    type        = string
    default     = "ami-0c55b159cbfafe1f0"
}

variable "subnet_id" {
    description = "The ID of the subnet to launch the EC2 instance in"
    type        = string
    default     = "subnet-0123456789abcdef"
}

variable "security_group_ids" {
    description = "A list of security group IDs to associate with the EC2 instance"
    type        = list(string)
    default     = ["sg-0123456789abcdef"]
}

variable "key_name" {
    description = "The name of the key pair to use for the EC2 instance"
    type        = string
    default     = "my-key-pair"
}

variable "aws_region" {
    description = "The AWS region to launch the EC2 instance in"
    type        = string
    default     = "us-west-2"
}
