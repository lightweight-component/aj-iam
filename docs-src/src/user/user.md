---
title: MCP Server SDK Usage
subTitle: 2024-12-05 by Frank Cheung
description: MCP Server SDK Usage
date: 2022-01-05
tags:
  - MCP Server SDK Usage
layout: layouts/docs.njk
---

# The User System

As part of a user system, the following core modules are essential and cannot be overlooked:

- User registration
- User login and logout
- User management

## Designing a Reusable User System

Almost every application requires a user system, and it's clearly inefficient — if not impractical — to reimplement one from scratch for each new project. Doing so would result in redundant work and inconsistent implementations across systems.

Therefore, the natural approach is to design a reusable user module that can be applied across multiple applications. The key question then becomes: What is the best architectural approach for such a system?

There are two common strategies:

## User SDK Approach

Provide a user system as an SDK (Software Development Kit) that can be integrated directly into each application. In this model:

- Each application manages its own user data, or shares it with a central source.
- Authentication and user management logic are encapsulated within the SDK.
- This approach offers tight integration and flexibility, especially useful when different applications have slightly different user requirements.

##  User Center (Centralized Service) Approach

Design a standalone User Center that operates independently of any specific application:

- It runs as a separate service, maintaining its own user data store but on top of view it's Unified Data.
- Applications access user-related functionality through standardized interfaces such as HTTP APIs or RPC calls.
- This decouples user management from business applications, enabling centralized control, unified authentication, and easier scaling.

When using the SDK approach, implementing Single Sign-On (SSO) can be challenging, as user data is typically managed independently within each application. This decentralized nature makes it difficult to maintain a unified identity across services.

On the other hand, most modern user systems are implemented as centralized services, which provide a single source of truth for user data and authentication. Based on this observation, we have decided to adopt the centralized service approach for our user system.

That said, this doesn’t mean the SDK approach is without merit. 
In certain modules such as access control or permission management, an SDK-based solution may still be a better fit, depending on the system’s architecture and integration requirements.

In the following sections, we will discuss these modules in detail, along with related components and their
  integration with AJ-IAM.

## User Registration

User registration refers to the process of creating a new user account for an application. In a centralized user center architecture, the application itself does not store core user data such as username, email, phone number, or password. Instead, this data is managed and stored exclusively by the user center.

The user center is responsible for handling user registration, authentication (login), and access control. It verifies whether a user is valid and communicates this information back to the application.

However, a common challenge arises when the database schema of the user center ***does not meet the specific needs*** of the application. Since the structure of the user center's database is often fixed, it may not be feasible to adapt or extend its fields to match the application’s requirements.

One possible workaround is to add a JSON-type field in the user table to store arbitrary data. While this can be useful for unstructured or dynamic data, it is **not ideal for structured data**, as it sacrifices query performance, type safety, and maintainability.
Recommended Solution

A better approach is to store the **application-specific user information locally** within the application's own database after the user has been registered in the user center. To link the local user data with the central user record, you can include a field such as `iam_id` that references the user ID from the user center.

This way:

- The core identity and authentication remain under the control of the user center.
- The application can store and manage extended user attributes tailored to its business needs.
- There is a clean separation between identity management and application-specific data.
