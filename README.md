# Infowijs assignment


This repository contains all code for the Infowijs assignment. The root folder contains:

* `backend`
    * a Maven/Kotlin/Vert.x project
* `frontend`
    * a Vite + React + TypeScript + Tailwind + HeroUI project
* `.github`
    * a folder with a simple GitHub action pipeline that runs code analysis on the backend project 
* `docker-compose.yml`: a docker compose file for running things

## How to run

After cloning, simply run the following command from the folder root:

`docker compose up -d frontend`

This starts up three containers (frontend, backend, database) provided that the ports are available.

You may now visit:

* A backend endpoint that returns an appointment that was seeded into the database: http://localhost:8888/appointment/ef9eeb02-4aa3-4d69-98d9-02db0619cd1b
* A frontend page where you can register attendees: http://localhost:8887/appointment/ef9eeb02-4aa3-4d69-98d9-02db0619cd1b
* A frontend page where you can create new appointments (readonly, no calls to backend): http://localhost:8887/appointment/create


# Choices and rationale

In general, I've treated this assignemnt as a full stack exercise. It's my way of showing a little bit of everything. As for the backend, I made the decision to try and make something with Vert.X, because that would be close to the actual stack used. I figured this would give me the opportunity to learn something new and show some critical thinking when it comes to choices in the backend. It probably would've been easier and faster to scaffold something like Spring boot and get everything out of the box, but where is the fun in that?

In total, I've spent about 15~ hours on this assignment (about 8 on the backend, rest on the frontend, database and getting everything to work together properly).

My priorities were to deliver something that:

* has some meaningful functionality
* can be demo'ed with a single command
* allows me to show some critical thinking

## Backend

Most of the time spent on the backend was reading and understanding Vert.X and what it can and can't do. Like React, it's quite a barebones toolkit/framework and you can add pretty much anything to it. Unlike Spring boot, it's not opinionated and you can do stuff however you like. I wanted to deliver something that works, so I focused on a simple approach. I did look into options and alternatives, but I'm not a guru in the JVM ecosystem so there was no time to learn/find/implement all these options. I will elaborate on some of these:

* There is no built-in Dependency Injection mechanism. It's possible to add libraries that offer this functionality, but the Vert.x paradigm seems to be small and lightweight. Most DI containers use reflection and don't really fit this paradigm, although [Micronaut](https://micronaut.io/) looks promising. I ended up doing dependency inversion manually.

* When it comes to ORM, the options are a bit limited. There is a reactive API for hibernate, but it requires non-final members for proxying and lazy loading. That doesn't work well with Kotlin, which promotes immutability. [Sqldelight](https://github.com/sqldelight/sqldelight) looks promising, but I ended up going the simple route and used SqlClientTemplates. They work for simple use cases, but there is a lot of manual SQL writing, mapping, not an easy way of doing transactions etc.

* Appointments have a code (UUID) property for sharing purposes. In a real system, people would probably be logged in and authorized to view something instead of hiding it behind a hard to guess id (or maybe not :)).

Given more time, I would have added:

* a nice ORM (with entities, repositories etc) 
* authentication
* a decent DI container
* unit/integration/e2e tests
* architecture diagrams
* data binding instead of manually parsing JSON
* logging (maybe OpenTelemetry)
* integration with AWS (ECS, S3, etc)
* Swagger-like API docs
* ... and so on


## Frontend

I didn't spend too much time on the frontend. I scaffolded a [HeroUI](https://www.heroui.com/) project with Vite + React + TypeScript + Tailwind. I added React query and removed a bunch of generated code (but not everything). The interesting parts to look at:

* `src/components/CreateAppointment.tsx`
* `src/components/ViewAppointment.tsx`
* `src/components/RegisterAttendee.tsx`

Given more time, I would have added:

* better UX in general
* more error handling
* a few test cases
* more advanced state management