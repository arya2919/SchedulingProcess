# Process Scheduling and Banker's Algorithm

## Overview

This project implements various process scheduling algorithms and the Banker's algorithm for deadlock avoidance.

## Contents

1. [Introduction](#introduction)
2. [Process Scheduling Algorithms](#process-scheduling-algorithms)
   - [First-Come, First-Served (FCFS)](#first-come-first-served-fcfs)
   - [Shortest Job Next (SJN)](#shortest-job-next-sjn)
   - [Round Robin (RR)](#round-robin-rr)
3. [Banker's Algorithm](#bankers-algorithm)
4. [Usage](#usage)
5. [References](#references)

## Introduction

Process scheduling is the mechanism by which an operating system decides which process runs at a given time. The Banker's algorithm is used for deadlock avoidance and resource allocation in operating systems.

## Process Scheduling Algorithms

### First-Come, First-Served (FCFS)

The FCFS scheduling algorithm schedules processes in the order they arrive in the ready queue.

### Shortest Job Next (SJN)

The SJN scheduling algorithm selects the process with the shortest burst time to execute next.

### Round Robin (RR)

The Round Robin scheduling algorithm assigns a fixed time quantum to each process in the ready queue and cycles through them.

## Banker's Algorithm

The Banker's algorithm is a resource allocation and deadlock avoidance algorithm that tests for the safety of granting resource requests. The algorithm checks if allocating resources leaves the system in a safe state.

## Usage

To run the project, compile and execute the code using the following commands:

