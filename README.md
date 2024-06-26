# Kitchen Delivery System

This project simulates a kitchen delivery system where orders are prepared, dispatched to couriers, and delivered based on different strategies.

## Project Structure

The project consists of several Java classes organized into packages:

- **com.cloud.kitchen**: Contains the main application classes and entry point.
- **com.cloud.kitchen.factory**: Includes the `CourierFactory` class for creating courier objects.
- **com.cloud.kitchen.mediator**: Contains the `KitchenMediator` class responsible for managing orders, couriers, and dispatch strategies.
- **com.cloud.kitchen.models**: Includes the `Order` and `Courier` classes representing orders and couriers respectively.
- **com.cloud.kitchen.observer**: Defines observer interfaces (`MediatorSubject`, `OrderReadyObserver`, `CourierArrivalObserver`) for handling events related to orders and couriers.
- **com.cloud.kitchen.strategy**: Contains different strategies (`OrderDispatcherStrategy`, `MatchedOrderDispatcherStrategy`, `FifoOrderDispatcherStrategy`) for dispatching orders.

## Setup and Execution

To run the project, follow these steps:

### Prerequisites

- Java Development Kit (JDK) 8 or higher installed
- Maven build tool installed

### Steps

1. **Clone the repository:**

   ```bash
   cd kitchen-delivery-system

2. **Build the project:**

     ```bash
   mvn clean install

3. **Run the main application:**
Replace target/kitchen-delivery-system.jar with the actual path to the generated JAR file if it differs.

    ```bash
   java -cp target/kitchen-delivery-system.jar com.cloud.kitchen.Main

4. **Testing:**
Unit tests are available in the src/test directory. You can run them using Maven:
    ```bash
   mvn test

5.  **Expected Output:**
The application simulates order preparation, courier dispatch, and delivery based on configured strategies.
Logs will show information about order handling, dispatch times, and average wait times for food and couriers.

  
6.   **Troubleshooting:**
If any errors occur during setup or execution, check Maven dependencies and ensure the ORDERS_FILE (dispatch_orders.json) is accessible in the expected location (src/main/resources).


7.  **Customization:**
Modify dispatch strategies (FifoOrderDispatcherStrategy, MatchedOrderDispatcherStrategy) or add new strategies in com.cloud.kitchen.stragety package as per your requirements.