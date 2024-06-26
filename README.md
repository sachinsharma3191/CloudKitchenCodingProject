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


## Download and Install Java:

1. Visit [Oracle's Java SE Downloads page](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or [OpenJDK's AdoptOpenJDK](https://adoptopenjdk.net/) to download the JDK appropriate for your operating system.
2. Follow the installation instructions provided for your OS.

## Download and Install Maven:

1. Visit [Apache Maven's download page](https://maven.apache.org/download.cgi) and download the latest Maven binary zip file.
2. Extract the downloaded zip file to a directory of your choice.

## Set up the MAVEN_HOME and PATH environment variables:

### Windows:
    setx MAVEN_HOME "path_to_your_maven_directory"
    setx PATH "%PATH%;%MAVEN_HOME%\bin"

Close and reopen any command prompt windows after setting the environment variables.

### Mac/Linux:
Add the following lines to your ~/.bash_profile or ~/.bashrc file:

    export MAVEN_HOME=path_to_your_maven_directory
    export PATH=$PATH:$MAVEN_HOME/bin

Run source ~/.bash_profile or source ~/.bashrc to apply the changes in the current terminal.


## Setup and Execution

To run the project, follow these steps:

### Prerequisites

- Java Development Kit (JDK): Version 8 or above. You can download it from Oracle's JDK website or use OpenJDK
- Maven build tool installed



### Steps

1. **Clone the repository:**

   ```bash
   cd CloudKitchenCodingProject

2. **Build the project:**

     ```bash
   mvn clean install

3. **Run the main application:**
Replace target/kitchen-delivery-system.jar with the actual path to the generated JAR file if it differs.

    ```bash
   java -cp target/CloudKitchenCodingProject-1.0.jar com.cloud.kitchen.Main

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