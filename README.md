# Here i will explain the process of the jwt auth

1 - first a client sends an http request.
2 - the first thing that gets executed in a spring application is the filter (JwtAuthFilter).
3 - in the jwt filter we will check if the request have the token , if not (send to client -> missing JWT token).
  - if the token is prsent the validation will start , the filter will first make a call using the UserDetails service to try to fetch the user informations from the database based on the user email , username extarcted from the JWT.
  - once the user is fetched the response is either the user exists or not , and if the user doesn't exist we will send -> user does't exist to the client witha 4003 error code.
  - but if everything is fine and we get our user from the database we will then start our validation process to check if the user authentic through the JwtService.
  - if the token isn't valid (expired , doesn't belong to that user...), we will send -> invalid JWT token.
  - otherwise what will happen is that we update the SecurityContextHolder and set the connected user , wich tells our filter chain that our user is authenticated.
  - once the SecurityContextHolder is updated i will automaticaly send the request to the dispatch servlet , and this one will dispatch th erequest to the correct controller.