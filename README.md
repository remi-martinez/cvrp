# CVRP 🚚

[![JavaFX](https://img.shields.io/badge/☕%20JavaFX-3a75b0.svg?style=for-the-badge&logo=)](https://openjfx.io/)
[![SceneBuilder](https://img.shields.io/badge/SceneBuilder-f4ac14.svg?style=for-the-badge&5&logo=data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiPz48IS0tIFVwbG9hZGVkIHRvOiBTVkcgUmVwbywgd3d3LnN2Z3JlcG8uY29tLCBHZW5lcmF0b3I6IFNWRyBSZXBvIE1peGVyIFRvb2xzIC0tPgo8c3ZnIA0KICB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciDQogIHdpZHRoPSIyNCINCiAgaGVpZ2h0PSIyNCINCiAgdmlld0JveD0iMCAwIDI0IDI0Ig0KICBmaWxsPSJub25lIg0KICBzdHJva2U9IiMwMDAwMDAiDQogIHN0cm9rZS13aWR0aD0iMiINCiAgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIg0KICBzdHJva2UtbGluZWpvaW49InJvdW5kIg0KPg0KICA8cGF0aCBkPSJNMjIgMTRhOCA4IDAgMDEtOCA4IiAvPg0KICA8cGF0aCBkPSJNMTggMTF2LTFhMiAyIDAgMDAtMi0ydjBhMiAyIDAgMDAtMiAydjAiIC8+DQogIDxwYXRoIGQ9Ik0xNCAxMFY5YTIgMiAwIDAwLTItMnYwYTIgMiAwIDAwLTIgMnYxIiAvPg0KICA8cGF0aCBkPSJNMTAgOS41VjRhMiAyIDAgMDAtMi0ydjBhMiAyIDAgMDAtMiAydjEwIiAvPg0KICA8cGF0aCBkPSJNMTggMTFhMiAyIDAgMTE0IDB2M2E4IDggMCAwMS04IDhoLTJjLTIuOCAwLTQuNS0uODYtNS45OS0yLjM0bC0zLjYtMy42YTIgMiAwIDAxMi44My0yLjgyTDcgMTUiIC8+DQo8L3N2Zz4=)](https://gluonhq.com/products/scene-builder/)


> Capacitated Vehicle Routing Problem solving with metaheuristics

The capacitated vehicle routing problem (also known as a CVRP) is one of many vehicle routing problems (VRPs.) These are a set of challenges that trucking and delivery businesses face in which route optimization is needed but challenging to obtain. This specific VRP, the CVRP, focuses on creating the ideal route for each vehicle of a fleet so that the maximum amount of deliveries takes place and the maximum amount of cargo space is used.

There are many variables to consider for the optimization problem. Each vehicle has a limited carrying capacity. In addition, other factors like fuel usage, driver schedule, and priority must be considered with each package and route.

In the ideal world, solving the problem means each driver and truck using 100% of the capacity and minimizing fuel usage or travel time to create maximum profits. However, it never works out like that. In reality, one truck might only use 60% of its capacity while using a lot of fuel, while another uses 90% and an average amount of fuel.

---

In this project, we are trying to solve this problem, using several algorithm with Java :
* Simulated Annealing
* Tabu search

Using those elementary transformations :
* Intra-relocate
* Extra-relocate
* Exchange
* 2-opt


# Launching the program

We realized this project using the IDE [IntelliJ IDEA Ultimate 2021.2.3](https://www.jetbrains.com/fr-fr/idea/download/#section=windows).

Java version used : **openjdk-18.0.1**

The project has been initialized with Maven for dependencies management : see `pom.xml` file.

**Dependencies:**

- JavaFX 17.0.2 for the graphical interface, compatible with Mac
- OpenCSV 5.6 for easier export to CSV files


## Graphic interface

We have built the GUI with [JavaFX](https://openjfx.io/) and [SceneBuilder](https://gluonhq.com/products/scene-builder/).

To test the GUI, you can pass the following variable to `false` in the main `App.java` file:

```java
private static final boolean AUTOSTART_SIMULATIONS = true;
```

This will make sure that the simulations to the CSV files are not started when the application is launched and the GUI will be launched.

This interface allowed us to illustrate our graphs in order to be able to inspect them other than by lines in a console.

![Untitled 7](https://user-images.githubusercontent.com/64494563/173204384-84f85a26-644c-4d6f-a5ec-993772998e4b.png)


**Operation:**

1. Import a graph with the Choose a file button
2. Choose an algorithm type in the drop-down list
3. Start the simulation

**Other features:**

- You can zoom in/out on the graph with the mouse wheel or with the zoom bar at the bottom left
- You can move freely in the graph with the mouse
- The coordinates of the mouse in the graph are written at the bottom left
- It is possible to hover over a client to see its coordinates:
    
![Untitled 8](https://user-images.githubusercontent.com/64494563/173204387-330e071f-4bf9-4e76-bb64-9a6fe5236d2d.png)

- In the settings tab you have several choices of options:
    
    ![Untitled 9](https://user-images.githubusercontent.com/64494563/173204392-2e96b988-97b0-4139-8b72-ff3852fc5d06.png)
    

- It is possible to show/hide the arrows of the graph
- It is possible to display the graph in black and white or in color
- Use the button Recenter the graph if you have moved it too far by mistake
- Expanding the graph allows you to "spread" it for a better readability

# Export to CSV file

When the program is started with `AUTOSTART_SIMULATIONS = true;` all simulations in the `main` function will start consecutively.

### Exported file

During the launching of these simulations, a CSV file is created under the directory `/files/exports`.

It respects the following nomenclature:

`Name algorithm_nbIterations_parameter1_parameter2.csv`

The parameters are optional and vary according to the algorithm used.

The separator used is the semicolon (`;`) and the decimals are written in the form `#,##`.

**Examples:**

- Tabu launched with 10 000 iterations and a taboo list size of 1 will be named :
Tabou_10000_1.csv

- Simulated annealing launched with 10 000 iterations, a variation of 0.9 and a temperature of 10 will be named :
Simulated annealing_10000_0.9_10.csv

**CSV file columns:**

- File name = The name of the imported graph (A3205.txt)
- Nb. of clients = The number of clients (nodes) in the graph
- Base fitness = The fitness (total distance of all vehicles) before simulation
- Nb vehicles min = The minimum number of vehicles before simulation
- Metaheuristic = Algorithm used (here Taboo or Simulated Annealing)
- Fitness result = The fitness after simulation
- Vehicles result = The minimum number of vehicles after simulation
- Number of iterations = The number of iterations to execute
- Execution time = Execution time using the chosen algorithm with this graph (in ms)
- Fitness improvement = The difference between the base fitness and the result fitness ($f_{base}-f_{result})$
- Tabu list (Tabu only) = Size of the tabu list passed in parameter
- Variation $\mu$ (Simulated annealing only) = Variation of the annealing passed in parameter
- Temperature (Simulated annealing) = Temperature of the annealing passed in parameter

---

© 2026 - CC BY SA NC - [Rémi MARTINEZ](https://github.com/remi-martinez/),  [Baptiste AUBERT](https://github.com/bapttiste73/)

