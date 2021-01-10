"""
This module implements APIs for link-developers.

Author: Yoonyoul Yoo
Date: 2021/01/09
"""

from uplink import Consumer, get
from uplink.clients.io import RequestTemplate


class CustomRequestTemplate(RequestTemplate):
    """A Python client for link-developers API."""

    def before_request(self, request):
        """Handle request beore it is being sent."""
        print("request" + request)
        return self._get_transition(
            RequestTemplate.before_request.__name__, request
        )


class ApiClient(Consumer):
    """A Python client for link-developers API."""

    @get("/v1/time")
    def time(self):
        """Retreives the time value from server."""
