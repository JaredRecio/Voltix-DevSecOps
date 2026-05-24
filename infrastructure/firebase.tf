# Infrastructure as Code - Voltix
# Definición de infraestructura con Terraform

terraform {
  required_version = ">= 1.0"
}

variable "project_id" {
  description = "ID del proyecto Firebase"
  default     = "voltix-app-dev"
}

variable "region" {
  description = "Región de despliegue"
  default     = "us-central1"
}

resource "google_firebase_project" "voltix" {
  project = var.project_id
}

resource "google_firestore_database" "voltix_db" {
  project     = var.project_id
  name        = "(default)"
  location_id = var.region
  type        = "FIRESTORE_NATIVE"
}

output "proyecto_id" {
  value = var.project_id
}

output "region_despliegue" {
  value = var.region
}
