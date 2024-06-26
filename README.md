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


## CLI Setup and Execution

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
Replace target/CloudKitchenCodingProject-1.0.jar with the actual path to the generated JAR file if it differs.

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


## Running Java Project in IntelliJ IDEA

### Prerequisites
1. **Java Development Kit (JDK)**:
    - Ensure JDK is installed. You can download it from [Oracle's Java SE Downloads](https://www.oracle.com/java/technologies/javase-downloads.html) or [OpenJDK](https://adoptopenjdk.net/).

2. **IntelliJ IDEA**:
    - Download and install IntelliJ IDEA from [here](https://www.jetbrains.com/idea/).

### Setting Up the Project

1. **Open Project**:
    - Launch IntelliJ IDEA.
    - Click on **File -> Open** and navigate to your Java project directory.

2. **Import Project from Existing Sources**:
    - Select the `pom.xml` file (Maven projects) or the root directory containing your Java project.
    - Click **Open** to import the project.

3. **Configure JDK in IntelliJ IDEA**:
    - Go to **File -> Project Structure**.
    - Under **Project Settings**, select **Project**.
    - Set the **Project SDK** to the installed JDK.

4. **Configure Maven in IntelliJ IDEA** (for Maven projects):
    - Go to **File -> Settings -> Build, Execution, Deployment -> Build Tools -> Maven**.
    - Set **Maven home directory** to the directory where Maven is installed.

### Running the Project

1. **Run the Main Class**:
    - In the Project tool window, navigate to your main class (usually located in `src/main/java`).
    - Right-click on the main class and select **Run** or **Debug**.

2. **View Output**:
    - The output will be displayed in the Run tool window at the bottom of IntelliJ IDEA.

### Debugging the Project

1. **Set Breakpoints**:
    - Click in the gutter next to a line of code to set a breakpoint.

2. **Debug Java Application**:
    - Open the Java file with breakpoints set.
    - Right-click in the editor and choose **Debug**.
    - Use the debug controls (`F8` for step over, `F7` for step into, etc.).

### Additional Resources

- [IntelliJ IDEA Documentation](https://www.jetbrains.com/idea/documentation/)
- [Debugging in IntelliJ IDEA](https://www.jetbrains.com/idea/features/debugging.html)



## Running Java Project in Visual Studio Code

### Prerequisites
1. **Java Development Kit (JDK)**:
    - Ensure JDK is installed. You can download it from [Oracle's Java SE Downloads](https://www.oracle.com/java/technologies/javase-downloads.html) or [OpenJDK](https://adoptopenjdk.net/).

2. **Visual Studio Code**:
    - Download and install Visual Studio Code from [here](https://code.visualstudio.com/).

3. **Java Extension Pack**:
    - Open Visual Studio Code.
    - Go to Extensions (or press `Ctrl+Shift+X`), search for "Java Extension Pack", and click "Install".

### Setting Up the Project

1. **Open Project Folder**:
    - Launch Visual Studio Code.
    - Click on **File -> Open Folder** and select your Java project folder.

2. **Configure JDK in Visual Studio Code**:
    - Open the Command Palette (`Ctrl+Shift+P` or `Cmd+Shift+P` on macOS).
    - Type "Java: Configure Java Runtime" and select it.
    - Choose the installed JDK.

3. **Create or Open Java Files**:
    - Navigate to your Java source code (`src` folder).
    - Create new Java files or open existing ones.

### Running the Project

1. **Run Java Application**:
    - Open the Java file containing the main method.
    - Right-click in the editor and choose **Run** or **Debug**.
    - Alternatively, use the shortcut `Ctrl+F5` to run without debugging.

2. **View Output**:
    - The output will be displayed in the Terminal tab at the bottom of Visual Studio Code.

### Debugging the Project

1. **Set Breakpoints**:
    - Click in the gutter next to a line of code to set a breakpoint.

2. **Debug Java Application**:
    - Open the Java file with breakpoints set.
    - Right-click in the editor and choose **Debug**.
    - Use the debug controls (`F5` to start debugging, `F10` for step over, `F11` for step into, etc.).

### Additional Resources

- [Visual Studio Code Java Documentation](https://code.visualstudio.com/docs/languages/java)
- [Debugging in Visual Studio Code](https://code.visualstudio.com/docs/editor/debugging)


## Running Java Project in Eclipse

### Prerequisites
1. **Java Development Kit (JDK)**:
    - Ensure JDK is installed. You can download it from [Oracle's Java SE Downloads](https://www.oracle.com/java/technologies/javase-downloads.html) or [OpenJDK](https://adoptopenjdk.net/).

2. **Eclipse IDE**:
    - Download and install Eclipse IDE for Java Developers from [here](https://www.eclipse.org/downloads/packages/).

### Setting Up the Project

1. **Open Eclipse**:
    - Launch Eclipse IDE.

2. **Import Project**:
    - If you have an existing project:
        - Click on **File -> Import -> General -> Existing Projects into Workspace**.
        - Select your project directory and click **Finish**.
    - If you are creating a new project:
        - Click on **File -> New -> Java Project**.
        - Enter a project name and click **Finish**.

3. **Configure JDK in Eclipse**:
    - Go to **Window -> Preferences -> Java -> Installed JREs**.
    - Add or select the JDK installed on your system.

### Running the Project

1. **Run the Main Class**:
    - In the Project Explorer, navigate to your main class (usually located in the `src` folder).
    - Right-click on the main class and select **Run As -> Java Application**.

2. **View Output**:
    - The output will be displayed in the Console view at the bottom of Eclipse.

### Debugging the Project

1. **Set Breakpoints**:
    - Click in the margin next to a line of code to set a breakpoint.

2. **Debug Java Application**:
    - Open the Java file with breakpoints set.
    - Right-click in the editor and choose **Debug As -> Java Application**.
    - Use the debug controls (`F8` for step over, `F5` for step into, etc.).

### Additional Resources

- [Eclipse Documentation](https://www.eclipse.org/documentation/)
- [Debugging in Eclipse](https://help.eclipse.org/latest/topic/org.eclipse.jdt.doc.user/reference/views/debug/ref-debug_view.htm)

