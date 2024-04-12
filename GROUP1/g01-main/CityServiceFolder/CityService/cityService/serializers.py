from rest_framework_gis.serializers import GeoModelSerializer

from cityService.models import City, Sensor, StaticSensor, MobileSensor


class CitySerializer(GeoModelSerializer):
    class Meta:
        model = City
        geo_field = "gps"
        fields = '__all__'


class SensorSerializer(GeoModelSerializer):
    class Meta:
        model = Sensor
        fields = '__all__'


class StaticSensorSerializer(SensorSerializer):
    class Meta(SensorSerializer.Meta):
        model = StaticSensor
        geo_field = "gps"
        fields = SensorSerializer.Meta.fields


class MobileSensorSerializer(SensorSerializer):
    class Meta(SensorSerializer.Meta):
        model = MobileSensor
        fields = SensorSerializer.Meta.fields
