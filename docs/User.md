# User API Spec

## register user

-   Endpoint : `POST /api/users`

Request body :

```json
{
    "username": "test",
    "password": "123",
    "name": "test"
}
```

Response body (success):

```json
{
    "data": "OK"
}
```

Response body (failed):

```json
{
    "errors": "<return errors>, ???"
}
```

## login user

-   Endpoint : `POST /api/auth/login`

Request body :

```json
{
    "username": "test",
    "password": "123"
}
```

Response body (success):

```json
{
    "data": {
        "token": "TOKEN",
        "expiredAt": 2313134332342 // miliseconds
    }
}
```

Response body (failed):

```json
{
    "errors": "<return errors>, ???"
}
```

## update user

-   Endpoint : `PATCH /api/users/current`

Request Header :

-   X-API-TOKEN : Token (Mandatory)

Request body :

```json
{
    "password": "123",
    "name": "newtest"
}
```

Response body (success):

```json
{
    "data": {
        "username": "test",
        "name": "newtest",
        "password": "123"
    }
}
```

Response body (failed):

```json
{
    "errors": "<return errors>, ???"
}
```

## get user

-   Endpoint : `GET /api/users/current`

Request Header :

-   X-API-TOKEN : Token (Mandatory)

Response body (success):

```json
{
    "data": {
        "id": "random-string",
        "username": "test",
        "name": "test"
    }
}
```

Response body (failed):

```json
{
    "errors": "<return errors>, ???"
}
```

## logout user

-   Endpoint : `DELETE /api/auth/logout`

Request Header :

-   X-API-TOKEN : Token (Mandatory)

Response body (success):

```json
{
    "data": "OK"
}
```
