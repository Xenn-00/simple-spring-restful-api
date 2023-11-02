# Contact API Spec

## Create Contact

-   Endpoint : `POST /api/contacts`

Request Header :

-   X-API-TOKEN = Token (Mandatory)

Request Body :

```json
{
    "firstname": "test",
    "lastname": "test",
    "email": "test@test.com",
    "phone": "+812331311332"
}
```

Response Body (success) :

```json
{
    "data": {
        "id": "random-string",
        "firstname": "test",
        "lastname": "test",
        "email": "test@test.com",
        "phone": "+812331311332"
    }
}
```

Response Body (failed) :

```json
{
    "errors": "<return errors> ???"
}
```

## Update Contact

-   Endpoint : `PUT /api/contacts/{idContact}`

Request Header :

-   X-API-TOKEN = Token (Mandatory)

Request Body :

```json
{
    "firstname": "test",
    "lastname": "test",
    "email": "test@test.com",
    "phone": "+812331311332"
}
```

Response Body (success) :

```json
{
    "data": {
        "id": "random-string",
        "firstname": "test",
        "lastname": "test",
        "email": "test@test.com",
        "phone": "+812331311332"
    }
}
```

Response Body (failed) :

```json
{
    "errors": "<return errors> ???"
}
```

## Get Contact

-   Endpoint : `GET /api/contacts/{idContact}`

Request Header :

-   X-API-TOKEN = Token (Mandatory)

Response Body (success) :

```json
{
    "data": {
        "id": "random-string",
        "firstname": "test",
        "lastname": "test",
        "email": "test@test.com",
        "phone": "+812331311332"
    }
}
```

Response Body (failed) :

```json
{
    "errors": "<return errors> ???"
}
```

## Search Contact

-   Endpoint : `GET /api/contacts`

Query Param :

-   name : String, contact firstname or lastname, using like query, optional
-   phone : String, contact phone, using like query, optional
-   email : String, contact email, using like query, optional
-   page : Integer, start from 0, default 0
-   size : Integer, default 10

Request Header :

-   X-API-TOKEN = Token (Mandatory)

Response Body (success) :

```json
{
    "data": [
        {
            "id": "random-string",
            "firstname": "test",
            "lastname": "test",
            "email": "test@test.com",
            "phone": "+812331311332"
        }
    , ...
    ],
    "paging" : {
        "currentPage" : 0,
        "totalPage" : 10,
        "size" : 10,
    }
}
```

Response Body (failed) :

```json
{
    "errors": "<return errors> ???"
}
```

## Remove Contact

-   Endpoint : `DELETE /api/contacts/{idContact}`

Request Header :

-   X-API-TOKEN = Token (Mandatory)

Response Body (success) :

```json
{
    "data": "OK"
}
```

Response Body (failed) :

```json
{
    "errors": "<return errors> ???"
}
```
