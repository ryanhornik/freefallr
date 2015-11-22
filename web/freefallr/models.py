from django.db import models
from django.contrib.auth.models import User
from django.db.models import Avg
from django.utils import timezone


class AppUser(models.Model):
    user = models.OneToOneField(User)

    def get_stats(self):
        stats = {}
        scores = Scores.objects.filter(app_user=self)
        scores_by_date = scores.order_by('date')
        stats['First_Fall'] = scores_by_date.first().info_str()
        stats['Last_Fall'] = scores_by_date.last().info_str()

        scores_by_time = scores.order_by('fall_time_ms')
        stats['Longest_Fall'] = scores_by_time.last().info_str()
        stats['Shortest_Fall'] = scores_by_time.first().info_str()

        stats['Average_Fall_Time'] = scores.aggregate(Avg('fall_time_ms'))['fall_time_ms__avg']
        return stats


class Scores(models.Model):
    fall_time_ms = models.IntegerField(null=False)
    app_user = models.ForeignKey(AppUser)
    date = models.DateTimeField(auto_now_add=True, null=True)

    def info_str(self):
        return str(self.fall_time_ms) + "ms " + self.date.strftime("%b %d, %Y %I:%M%p")