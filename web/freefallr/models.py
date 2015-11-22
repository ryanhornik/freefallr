from django.db import models
from django.contrib.auth.models import User


class AppUser(models.Model):
    user = models.OneToOneField(User)


class Scores(models.Model):
    fall_time_ms = models.IntegerField(null=False)
    app_user = models.ForeignKey(AppUser)
