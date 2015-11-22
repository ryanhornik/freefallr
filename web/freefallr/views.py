import json

from django.contrib.auth import authenticate, login
from django.contrib.auth.models import User
from django.http import HttpResponse
from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from django.views.decorators.debug import sensitive_post_parameters
from freefallr.models import AppUser, Scores


@csrf_exempt
@sensitive_post_parameters()
def login_view(request):
    if request.method == "POST":
        username = request.POST['username']
        password = request.POST['password']
        user = authenticate(username=username, password=password)
        if user is not None:
            login(request, user)
            return HttpResponse("Success")
    return HttpResponse("Invalid login", status=401)


@csrf_exempt
@sensitive_post_parameters()
def register(request):
    if request.method == "POST":
        email = request.POST['email']
        username = request.POST['username']
        password = request.POST['password']
        confirm_password = request.POST['confirm_password']
        if password != confirm_password:
            return HttpResponse("Passwords do not match", status=401)
        user = User.objects.create_user(username, email, password)
        if user is not None:
            app_user = AppUser(user=user)
            app_user.save()
            return HttpResponse("Success")
        else:
            return HttpResponse("Username Taken", status=401)
    return HttpResponse("Error", status=401)


@csrf_exempt
def submit_score(request):
    if request.method == "POST":
        score = Scores(fall_time_ms=request.POST['time'], app_user=request.user.appuser)
        score.save()
        return HttpResponse("Success")


def score_list(n):
    top_scores = Scores.objects.order_by('-fall_time_ms')[:n]
    as_list = []
    for score in top_scores:
        as_list.append({'username': score.app_user.user.username, 'score': score.fall_time_ms})
    return as_list


def top_n_scores(request):
    as_list = score_list(int(request.GET.get('n', 10)))
    return HttpResponse(json.dumps(as_list))


def leaderboard(request):
    scores = score_list(10)
    return render(request, 'leaderboard.html', {'scores': scores})


def user_stats(request):
    user_id = request.GET.get('uid', -1)
    if user_id == -1:
        usr = request.user.appuser
    else:
        usr = AppUser.objects.get(pk=user_id)
    return HttpResponse(json.dumps(usr.get_stats()))
