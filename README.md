# Payment App
This applications is using merchants and transactions. Transactions are calculated to pay a specific merchant some amount.

# How to use
1. Open command line/terminal
2. Enter the project directory
3. Run `maven clean install` and wait to finish the task
4. Run `docker-compose up --build` and to start the application
5. Download and install Postman
6. Import requests collection from **postman** directory

**Note:** Every request needs authentication. Default admin user is `admin` with password `pass`
  
# Import merchants with Postman
1. Select **Import merchants** request
2. Switch to **Body** tab
3. For key **file** select file **postman/merchant.csv** file
4. Click **Send** button

# Import users with Postman
1. Select **Import users** request
2. Switch to **Body** tab
3. For key **file** select file **postman/users.csv** file
4. Click **Send** button

# Create transaction with Postman
1. Select **Add transaction** request
2. Switch to **Body** tab
3. Paste following JSON structure (if not already present)

```json
{
    "uuid": "143e4567-e89b-12d3-a456-426655440000",
    "amount": 50,
    "status": "approved",
    "customerEmail": "krasi@test.com",
    "customerPhone": "123456789",
    "referenceId": "123dsfjhh34is"
}
```

4. Make sure to change **uuid** property for every request
5. Click Click **Send** button

# Connect to Postgres DB
1. Download pgAdmin tool
2. Add server and enter following details:

    host: localhost

    username: postgres

    password: password

    maintenance database: mydb

    port: 5432
3. Click **Save** button
