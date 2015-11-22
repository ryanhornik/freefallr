from django.conf.urls import include, url
from . import views

urlpatterns = [
    url(r'login/?$', views.login_view, name='login'),
    url(r'register/?$', views.register, name='register'),
    url(r'top/?$', views.top_n_scores, name='top'),
    url(r'leaderboard/?$', views.leaderboard, name='leaderboard'),
    url(r'submit/?$', views.submit_score, name='submit_score'),
]
