# Address API Spec

## Create Address

-   Endpoint : `POST /api/contacts/{idContact}/addresses`

Request Header :

-   X-API-TOKEN = Token (Mandatory)

Request Body :

```json
{
    "street": "street test",
    "city": "city test",
    "province": "province test",
    "country": "country test",
    "postalCode": "131313"
}
```

Response Body (success):

```json
{
    "data": {
        "id": "random-string",
        "street": "street test",
        "city": "city test",
        "province": "province test",
        "country": "country test",
        "postalCode": "131313"
    }
}
```

Response Body (failed):

```json
{
    "errors": "<return errors> ???"
}
```

## Update Address

-   Endpoint : `PUT /api/contacts/{idContact}/addresses/{idAddress}`

Request Header :

-   X-API-TOKEN = Token (Mandatory)

Request Body :

```json
{
    "street": "street test",
    "city": "city test",
    "province": "province test",
    "country": "country test",
    "postalCode": "131313"
}
```

Response Body (success):

```json
{
    "data": {
        "id": "random-string",
        "street": "street test",
        "city": "city test",
        "province": "province test",
        "country": "country test",
        "postalCode": "131313"
    }
}
```

Response Body (failed):

```json
{
    "errors": "<return errors> ???"
}
```

## Get Address

-   Endpoint : `GET /api/contacts/{idContact}/addresses/{idAddress}`

Request Header :

-   X-API-TOKEN = Token (Mandatory)

Response Body (success):

```json
{
    "data": {
        "id": "random-string",
        "street": "street test",
        "city": "city test",
        "province": "province test",
        "country": "country test",
        "postalCode": "131313"
    }
}
```

Response Body (failed):

```json
{
    "errors": "<return errors> ???"
}
```

## Remove Address

-   Endpoint : `DELETE /api/contacts/{idContact}/addresses/{idAddress}`

Request Header :

-   X-API-TOKEN = Token (Mandatory)

Response Body (success):

```json
{
    "data": "OK"
}
```

Response Body (failed):

```json
{
    "errors": "<return errors> ???"
}
```

## List Address

-   Endpoint : `GET /api/contacts/{idContact}/addresses`

Request Header :

-   X-API-TOKEN = Token (Mandatory)
    Response Body (success):

```json
{
    "data": [
        {
            "id": "random-string",
            "street": "street test",
            "city": "city test",
            "province": "province test",
            "country": "country test",
            "postalCode": "131313"
        }
    ]
}
```

Response Body (failed):

```json
{
    "errors": "<return errors> ???"
}
```
