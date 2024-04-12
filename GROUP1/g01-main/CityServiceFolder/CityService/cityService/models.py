from django.contrib.gis.db import models


class City(models.Model):
    name = models.CharField(max_length=20)
    gps = models.PointField()


class Sensor(models.Model):
    class SensorType(models.TextChoices):
        TEMPERATURE = 'Temperature'
        POSITION = 'Position'
        PRESSURE = 'Pressure'
        FORCE = 'Force'
        HUMIDITY = 'Humidity'

    type = models.CharField(choices=SensorType.choices, default=SensorType.TEMPERATURE, max_length=20)
    city = models.ForeignKey(City, on_delete=models.CASCADE)

    class Meta:
        abstract = True


class StaticSensor(Sensor):
    gps = models.PointField()


class MobileSensor(Sensor):
    pass
