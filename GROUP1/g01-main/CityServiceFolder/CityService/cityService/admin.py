from django.contrib import admin

from cityService.models import City, StaticSensor, MobileSensor

admin.site.register(City)
admin.site.register(StaticSensor)
admin.site.register(MobileSensor)
