# Distributed File Storage System

A Java-based distributed file storage system that stores files across multiple networked storage nodes and provides replication, concurrent access, failover, and recovery.

## Overview

This project demonstrates the core concepts behind a distributed storage system using Java.

The system consists of multiple storage nodes that communicate over TCP sockets. A coordinator selects the primary node for a file, while the replication manager maintains copies of the file on other nodes.

The system also handles node failures by redirecting operations to available replicas and supports resynchronization when a recovered node loses its data.

## Features

- File storage using local storage nodes
- PUT, GET and DELETE operations
- Atomic file increment operation
- Multiple networked storage nodes
- TCP client-server communication
- Primary node selection using file hashing
- Replication across multiple nodes
- Concurrent replication using thread pools
- Concurrent client writes
- Lost-update prevention
- Primary node failover for GET operations
- Primary node failover for PUT operations
- Replica recovery and resynchronization
- Node failure simulation and recovery testing

## Architecture

The system consists of the following main components:

### StorageNode

Responsible for storing, reading, deleting and updating files on local disk.

### StorageNodeServer

Runs a storage node as a network server and accepts requests from clients over TCP.

Supported commands include:

- `PUT`
- `GET`
- `DELETE`
- `INCREMENT`

### StorageClient

Provides the client-side interface for communicating with a storage node over the network.

### StorageNodeManager

Manages multiple storage nodes and their availability.

### StorageCoordinator

Selects the primary node for a file using hashing and handles failover when the preferred node is unavailable.

### NetworkReplicationManager

Stores data on the primary node and replicates it to secondary nodes concurrently.

It also supports replica resynchronization after a node loses its data.

## Project Structure

```text
src/main/java/storage/
│
├── StorageNode.java
├── StorageNodeServer.java
├── StorageClient.java
├── StorageNodeManager.java
├── StorageCoordinator.java
├── NetworkReplicationManager.java
├── NetworkNodeLauncher.java
│
├── ConcurrentWriteTest.java
├── LostUpdateTest.java
├── NetworkNodesTest.java
├── NetworkReplicationTest.java
├── RecoveryTest.java
├── FailoverTest.java
├── FailoverPutTest.java
└── FinalRecoveryTest.java