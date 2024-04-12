import json
from pprint import pprint

from cityService.producers import log_to_kafka
from decouple import config
from rest_framework.response import Response
from rest_framework.views import exception_handler
from django.http.response import HttpResponse
import requests


class LogKafkaMiddleware:
    def __init__(self, get_response):
        self.get_response = get_response
        # One-time configuration and initialization.

    def __call__(self, request):
        # Code to be executed for each request before
        # the view (and later middleware) are called.

        response = self.get_response(request)

        # Code to be executed for each request/response after
        # the view is called.

        if type(response) is Response:
            status_code = response.renderer_context['response'].status_code
            request_method = request.method
            if request_method == 'GET':
                operation_type = 'READ'
            elif request_method == 'PATCH':
                operation_type = 'UPDATE'
            elif request_method == 'POST':
                operation_type = 'CREATE'
            elif request_method == 'DEL':
                operation_type = 'DELETE'
            else:
                operation_type = 'OTHER'

            user = 'Anonymous'
            if not request.user.is_anonymous:
                user = request.user.name

            log_type = 'INFORMATION'
            if config('ENVIRONMENT') == 'Development':
                log_type = 'DEBUG'

            log_to_kafka(log_type, operation_type,
                         'Status code: ' + str(status_code) +
                         '\nUser: ' + user +
                         '\nPath: ' + request.path,
                         'CityService')

        return response


def custom_exception_handler(exc, context):
    # Call REST framework's default exception handler first,
    # to get the standard error response.
    response = exception_handler(exc, context)

    request = context["request"]
    request_method = request.method
    if request_method == 'GET':
        operation_type = 'READ'
    elif request_method == 'PATCH':
        operation_type = 'UPDATE'
    elif request_method == 'POST':
        operation_type = 'CREATE'
    elif request_method == 'DEL':
        operation_type = 'DELETE'
    else:
        operation_type = 'OTHER'

    user = 'Anonymous'
    if not request.user.is_anonymous:
        user = request.user.name

    log_to_kafka('WARNING', operation_type,
                 'Status code: ' + str(500) +
                 '\nUser: ' + user +
                 '\nPath: ' + request.path +
                 'Error: ' + str(exc),
                 'CityService')

    # Now add the HTTP status code to the response.
    if response is not None:
        response.data['status_code'] = response.status_code

    return response


class UserRoleMiddlerware:
    def __init__(self, get_response):
        self.get_response = get_response
        # One-time configuration and initialization.

    def __call__(self, request):
        # Code to be executed for each request before
        # the view (and later middleware) are called.
        if request.path == '/' or request.path == '/health':
            # No authorization required
            pass
        else:
            token = request.headers.get('Authorization')
            if token:
                # Send request to gateway /getUserFromToken to obtain the user role
                req = requests.post('https://cosn-api-gateway.herokuapp.com/getUserFromToken',
                                    json={"token": token})
                if req.status_code != 200:
                    return HttpResponse(json.dumps({"detail": "Unauthorized", "status_code": 401}),
                                    content_type="application/json", status=401)
                else:
                    role = req.json()['user']['role']['id']
                    request.role = role
            else:
                return HttpResponse(json.dumps({"detail": "Unauthorized", "status_code": 401}),
                                    content_type="application/json", status=401)

        response = self.get_response(request)

        # Code to be executed for each request/response after
        # the view is called.

        return response
