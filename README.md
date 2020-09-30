# order-service

Restful API requirements

Customer:
- create a customer
- update a customer
- remove a customer
- fetch a customer by Id

Product:
- create a product
- update a product by Id
- remove a product by Id
- get a product by Id
- get all the products

Payment(is always associated with a Customer):
- create a payment for a customer
- update a payment for a customer
- get the payment detail of a customer
- Remove a payment from a customer

Order:
- Create an order [With at least one product]
- Create an order [with multiple products]
- Update an order [add a product to existing order]
- update an order [remove a product from existing order]
- update an order [remove a product, if it's the last product then remove the order as well]
- remove an order 

- update the billing address
    - if adding a new address then replace the existing one
	- No removal, there should be at least one address associated
- update the shipping address
	- if adding a new address then replace the existing one
	- No removal, there should be at least one address associated
- fetch the status of order
- fetch the items in the order
- fetch sub-total of the oder
- fetch tax of the oder
- fetch shipping_charges of the order
- fetch total of the oder

Bulk Order API requirements:
- Create a bulk order
- fetch a bulk order
- Update a bulk order
- remove a bulk order