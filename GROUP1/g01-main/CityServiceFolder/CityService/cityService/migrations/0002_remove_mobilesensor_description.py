# Generated by Django 4.0.1 on 2022-01-13 18:54

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('cityService', '0001_initial'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='mobilesensor',
            name='description',
        ),
    ]
