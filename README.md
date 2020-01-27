# discount-store-app
This is an application to create customers and add items to their cart.
It also allows the user to remove or edit customers as well as their items.
All item prices are calculated upon fetching the user's data.
## CRUD API
The APIs have a base API 'customer-api/v1/'. 
### Create User
For creating a customer, call this API
```
POST /customers
BODY
{
    "name": "John Doe",
    "creationDate": "2018-01-01",
    "type": "employee"
}
```
A successful response is 201 (Created Successfully). \
The validation for the fields are as follows:
```
name => Not blank
creationDate => YYYY-MM-DD
type => ENUM(normal, employee, affiliate)
```
### Update Customer
For updating a customer, call this API
```
PUT /customers
BODY
{
    "name": "John Doe",
    "creationDate": "2018-01-01",
    "type": "employee"
}
```
A successful response is 200 (OK Successfully). \
The validation for the fields are as follows:
```
name => Not blank
creationDate => YYYY-MM-DD
type => ENUM(normal, employee, affiliate)
```
### Delete Customer
For deleting a customer, call this API
```
DELETE /customers/{id}
```
A successful response is 200 (OK Successfully).
### Get Customer
For getting a customer, call this API
```
GET /customers/{id}
```
A successful response is 200 (OK Successfully) along with the customer data.
### Get Customers
For getting the list of customers, call this API
```
GET /customers
```
A successful response is 200 (OK Successfully) along with the list customers.
### Create Item
For adding an item to a customer, call this API
```
POST /customers/{customerId}/items
BODY
{
    "name": "Fridge",
    "price": 120,
    "type": "electronic"
}
```
A successful response is 201 (Created Successfully). \
The validation for the fields are as follows:
```
name => Not blank
price => decimal
type => ENUM(other, grocery, electronic, fabric)
```
### Update Item
For updating a customer's item, call this API
```
PUT /customers/{customerId}/items/{itemId}
BODY
{
    "name": "Fridge",
    "price": 120,
    "type": "electronic"
}
```
A successful response is 200 (OK Successfully). \
The validation for the fields are as follows:
```
name => Not blank
price => decimal
type => ENUM(other, grocery, electronic, fabric)
```
### Delete Item
For deleting a customer's item, call this API
```
DELETE /customers/{customerId}/items/{itemId}
```
A successful response is 200 (OK Successfully).
### Get Item
For getting a customer's item, call this API
```
GET /customers/{customerId}/items/{itemId}
```
A successful response is 200 (OK Successfully) along with the item.
### Get Items
For getting a list of customer's items, call this API
```
GET /customers/{customerId}/items
```
A successful response is 200 (OK Successfully) along with the list of items.