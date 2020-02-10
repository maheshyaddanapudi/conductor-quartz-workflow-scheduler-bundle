# CQW Scheduler Bundle

conductor-quartz-workflow-scheduler-bundle

CQW Scheduler Bundle = Conductor Quartz Workflow Scheduler Bundle


The idea is to build a single suite of loosely coupled open source tools to perform scheduling i.e. triggering a Netflix Conductor workflow at a defined time or regular intervals of time, with a pre defined input, WITHOUT THE NEED TO CODE (To the maximum extent possible), by implementing


 • Angular UI to call the Web Services / APIs 

 • Spring Boot - Quartz Scheduler - embedded MariaDB support & external MySQL DB support.


The catch point being, it will be a UI and Scheduler Jar  which will enable Conductor Workflows to be scheduled by user using UI interface 

How ? 

By bridging the communication with RestFUL JSON based web services / APIs.

By combining Quartz with Conductor, the Workflows that are otherwise triggered manually are now automatically triggered in scheduled manner and Quartz does the Scheduler part.

Though the idea of making Scheduler for Conductor is good, it still needs a lot of knowledge on JSON, to write up the Web Service calls and lot of knowledge on Quartz, to define the Scheduler part.

With a customized unique UI, CQW Scheduler Bundle allows users to pick and choose Conductor Workflows visually and then define the inputs and configure the schedule at which the Conductor Workflow should be triggered with the input Predefined. Finally the UI allows users to a name and description for the schedule.

Once all done, the user can see the schedules built by UI and can pause / resume or delete the schedule.

Also a summary of run history of each schedule can be viewed in UI instead of connecting to database or mining logs.

Thus eliminating the need to code or have knowledge of coding / scripting / IT tools for development of Scheduler for Conductor.

## Pre-Requisites
1. openjdk8 or Java 8
2. Maven / mvn
3. Node JS / npm
4. @angular/cli@7

## Setup Instructions

 • Clone / Download and extract the Repo

 • To install necessary modules for Angular JS, navigate to conductor-scheduler-angular-ui directory and execute the npm install commands.

cd conductor-scheduler-angular-ui

npm i --save

npm i --save-dev --unsafe-perm node-sass

## Startup Instructions

 • To start the scheduler with embedded mariadb4j as persistence, use Maven run. 
To use a permanent Port for mariadb4j or persistent dataDir, please uncomment and provide corresponding details in application.yml
To use external mysql database as persistence unit, please change db=mysql and provide the DB url and driver class accordingly. The properties are already pasted , commented in application.yml file.

Also an important property to be configured in application.yml is conductor.server.api.endpoint , which internally is used to trigger the workflows through rest API call.

cd conductor-quartz-workflow-scheduler

mvn spring-boot:run

In case, you would like to package the jar first and then run.

mvn install

cd target

java -jar conductor-quartz-workflow-scheduler-0.0.1-SNAPSHOT.jar

To access the Swagger UI, please use the url sample below for reference.

http://localhost:1408/swagger-ui.html


 • To start the angular UI, use the below commands , for the development and prod mimic startup

cd conductor-scheduler-angular-ui

For Development mode: Fast compile time & slow runtime

PORT=1987 WF_SERVER=http://localhost:8080/api/ WF_SCHEDULER=http://localhost:1408/api/ npm run start

For Production mimic mode: Slow (Very) compile time & Fast runtime

PORT=1987 WF_SERVER=http://localhost:8080/api/ WF_SCHEDULER=http://localhost:1408/api/ npm run server

Please provide the Conductor Server URL and Scheduler Server URL accordingly.

Access the UI : http://localhost:1987/

## Motivation

There is a saying from recent times that "Every idea comes from a pain point"

Scheduler in daily IT life are more needed in areas where implementation of Microservice Orchestrators like Netflix Conductor etc. is done. As known, not everywhere is comfortable, easy to plugin scheduler without coding available.
For such scenarios, usually a dependency is created on external scheduler tools, usually org wide tools like Control M or Autosys. That is where Quartz came into picture to ease the scheduler dependency with some efficient standards. Then the final hurdle of making a framework which hosts the configured Schedules and executes the schedule on time or maintain a history of what happened to a configured schedule etc. 

To avoid all this pain of below listed, this idea of CQW Scheduler Bundle came up.

 • Learning to code at least moderately even though it's an easier JSON API

 • Developing an UI Scheduler framework

 • Learning new, though simple tools, like Quartz

 • Dependency on external schedulers 


## Features

 • View Netflix Conductor Workflows & Corresponding Metadata in Web UI

 • Trigger Netflix Conductor Workflows by providing runtime inputs in Web UI or using pure JSON payload.

 • Schedule Netflix Conductor Workflows with Predefined inputs in Web UI or using pure JSON payload, with a variety of Minute / Hourly / Daily / Weekly / Yearly scheduling options.

 • View a comprehensive list of all the Netflix Conductor Workflow trigger schedules, scheduled through Quartz & Web UI.

 • View a summary of run history of each and every Netflix Conductor Workflow trigger schedules, scheduled through Quartz & Web UI.


## Tech/framework used

 • conductor-server - Presumably downloaded from Maven repository

 • Java Spring Boot : Quartz + Mariadb4j

 • Angular JS 7


## Inspiration

 • Netflix Conductor

 • Quartz Scheduler


## Credits

 • Mahesh Yaddanapudi - zzzmahesh@live.com
