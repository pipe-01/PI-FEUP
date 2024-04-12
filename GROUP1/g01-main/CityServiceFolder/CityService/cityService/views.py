from cityService.models import City, StaticSensor, MobileSensor
from cityService.serializers import CitySerializer, StaticSensorSerializer, MobileSensorSerializer
from django.contrib.gis.measure import Distance
from rest_framework import viewsets
from rest_framework.decorators import action, api_view
from rest_framework.generics import get_object_or_404
from rest_framework.response import Response
from rest_framework import permissions


class IsResearcherPermission(permissions.BasePermission):
    """
    Checks if request has user role = 3
    """

    def has_permission(self, request, view):
        if request.role == 3:
            return True
        else:
            return False


class IsServiceProviderPermission(permissions.BasePermission):
    """
    Checks if request has user role = 4
    """

    def has_permission(self, request, view):
        if request.role == 4:
            return True
        else:
            return False


class IsAppDeveloperPermission(permissions.BasePermission):
    """
    Checks if request has user role = 5
    """

    def has_permission(self, request, view):
        if request.role == 5:
            return True
        else:
            return False


class CityViewSet(viewsets.ModelViewSet):
    permission_classes = [IsResearcherPermission | IsServiceProviderPermission]
    queryset = City.objects.all()
    serializer_class = CitySerializer


class StaticSensorViewSet(viewsets.ModelViewSet):
    permission_classes = [IsResearcherPermission | IsServiceProviderPermission]
    queryset = StaticSensor.objects.all()
    serializer_class = StaticSensorSerializer

    @action(detail=False, methods=['get'], url_path='city',
            permission_classes=[IsResearcherPermission | IsServiceProviderPermission | IsAppDeveloperPermission])
    def list_static_sensors_city(self, request):
        city = self.request.query_params.get('city_id')
        sensors = StaticSensor.objects.filter(city_id=city)
        serializer = StaticSensorSerializer(sensors, many=True)
        return Response(serializer.data)

    @action(detail=False, methods=['get'], url_path='city_type',
            permission_classes=[IsResearcherPermission | IsServiceProviderPermission | IsAppDeveloperPermission])
    def list_static_sensors_city_type(self, request):
        city = self.request.query_params.get('city_id')
        type = self.request.query_params.get('type')
        sensors = StaticSensor.objects.filter(city_id=city, type=type)
        serializer = StaticSensorSerializer(sensors, many=True)
        return Response(serializer.data)

    @action(detail=False, methods=['get'], url_path='radius',
            permission_classes=[IsResearcherPermission | IsServiceProviderPermission | IsAppDeveloperPermission])
    def list_static_sensors_radius(self, request):
        city_id = self.request.query_params.get('city_id')
        radius = self.request.query_params.get('radius')
        city = get_object_or_404(City, pk=city_id)
        sensors = StaticSensor.objects.filter(gps__distance_lt=(city.gps, Distance(km=radius)))
        serializer = StaticSensorSerializer(sensors, many=True)
        return Response(serializer.data)

    @action(detail=False, methods=['get'], url_path='radius_type',
            permission_classes=[IsResearcherPermission | IsServiceProviderPermission | IsAppDeveloperPermission])
    def list_static_sensors_radius_type(self, request):
        type = self.request.query_params.get('type')
        city_id = self.request.query_params.get('city_id')
        radius = self.request.query_params.get('radius')
        city = get_object_or_404(City, pk=city_id)
        sensors = StaticSensor.objects.filter(type=type, gps__distance_lt=(city.gps, Distance(km=radius)))
        serializer = StaticSensorSerializer(sensors, many=True)
        return Response(serializer.data)


class MobileSensorViewSet(viewsets.ModelViewSet):
    permission_classes = [IsResearcherPermission | IsServiceProviderPermission]
    queryset = MobileSensor.objects.all()
    serializer_class = MobileSensorSerializer

    @action(detail=False, methods=['get'], url_path='city',
            permission_classes=[IsResearcherPermission | IsServiceProviderPermission | IsAppDeveloperPermission])
    def list_mobile_sensors_city(self, request):
        city = self.request.query_params.get('city_id')
        sensors = MobileSensor.objects.filter(city_id=city)
        serializer = MobileSensorSerializer(sensors, many=True)
        return Response(serializer.data)

    @action(detail=False, methods=['get'], url_path='city_type',
            permission_classes=[IsResearcherPermission | IsServiceProviderPermission | IsAppDeveloperPermission])
    def list_mobile_sensors_city_type(self, request):
        city = self.request.query_params.get('city_id')
        type = self.request.query_params.get('type')
        sensors = MobileSensor.objects.filter(city_id=city, type=type)
        serializer = MobileSensorSerializer(sensors, many=True)
        return Response(serializer.data)


@api_view(['GET'])
def check_health(request):
    return Response(data={"details": "Service Running", "status_code": 200}, status=200)
