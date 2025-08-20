---
title: 用户系统的架构设计与实现策略-用户系统
subTitle: 2024-12-05 by Frank Cheung
description: 用户系统的架构设计与实现策略
date: 2022-01-05
tags:
  - 用户系统的架构设计与实现策略
layout: layouts/docs-en.njk
---
# User System Architecture Design and Implementation Strategy - User Model

A user system should encompass not only basic user business functions but also user permission design and its implementation. In this article, we will explore methodologies for designing and implementing user permissions.

# Introduction

In building modern application systems, few design decisions have as profound an impact on security, scalability, and user experience as access control mechanisms. Many development teams initially opt for a simple RBAC (Role-Based Access Control) model, embedding authorization logic directly into application code. However, as business requirements evolve, it often becomes necessary to integrate multiple mechanisms—RBAC, ABAC, and ReBAC—to meet the complex authorization needs of real-world scenarios.

Many people mistakenly believe these models are mutually exclusive choices, thinking enterprises need only select one and stick with it long-term. In reality, development teams often incrementally layer and adjust different control models based on changing business needs, forming more flexible and granular permission systems.

This article will guide you through an in-depth understanding of the three mainstream access control models—RBAC (Role-Based), ABAC (Attribute-Based), and ReBAC (Relationship-Based)—analyzing their respective advantages, disadvantages, and applicable scenarios. We'll help you determine which model—or combination of models—best fits your product requirements and provide practical implementation suggestions.

# Understanding Mainstream Access Control Models

Access control fundamentally answers a seemingly simple question: "Who can perform what operations on which resources?"  
The answer depends on user identity, resource organization, and contextual information (such as organizational affiliation, time, geographical location, etc.).

## Role-Based Access Control (RBAC)

RBAC (Role-Based Access Control) is currently the most widely used access control model. Its core idea is to assign permissions to "roles," while users inherit corresponding permissions by being assigned to specific roles. In the RBAC model, users have explicit roles that determine which data they can access. This model is particularly suitable for organizations with clear division of responsibilities and hierarchical structures. For example, in a content management system:

- Editors can create and edit content
- Reviewers can approve or reject content
- Administrators can manage users and system settings

RBAC's logic is clear and easy to understand, making it a common starting point for many development teams when implementing permission systems themselves. However, RBAC cannot meet the fine-grained permission control needs of modern applications. Consequently, many teams further introduce attribute-based or relationship-based models to extend their permission systems.

## Relationship-Based Access Control (ReBAC)

ReBAC (Relationship-Based Access Control) focuses on relationships between different entities within the system. It doesn't rely on direct authorization or attribute judgments but determines access permissions through associations between users and resources.

Let's continue with the content management system example mentioned in RBAC. Suppose a user creates a folder in the system and stores some documents within it. If you have viewing permission for that folder, you should also be able to view all documents within it. This requires introducing ReBAC: meaning you need to organize permissions not just by defining roles, but also based on relationships between resources—in this case, the ownership relationship between documents and folders.

ReBAC is particularly suitable for handling data scenarios with complex hierarchies and high interconnectivity. For instance, in a document management system, it can easily express rules like "users can access all documents from projects they participate in" without needing to tag each document individually.

## Attribute-Based Access Control (ABAC)

ABAC (Attribute-Based Access Control) uses a more dynamic approach to make access decisions, basing them on attributes of users, resources, operations, and environments. It is currently the most flexible access control model but also has the highest implementation and maintenance costs.

ABAC allows very fine-grained control through conditions and attributes. Returning to our content management system example, some documents might be labeled as "public," and regardless of which folder they're stored in, all users should have viewing rights. In this case, the "public" document attribute can be used as a judgment criterion in the authorization logic.

# RBAC vs. ABAC: When Role Permissions Are Insufficient

While RBAC provides a solid foundation for access control, it gradually reveals limitations as application functionality becomes increasingly complex. For example, the number of required "roles" may skyrocket, creating a heavy operational burden; or role granularity may be too coarse, granting unnecessary permissions and creating security risks.

## Limitations of RBAC

RBAC performs well when access control requirements align closely with organizational structure. However, it struggles in the following scenarios:

- Need to dynamically determine permissions based on context
- Existence of temporary access needs
- Requirement for finer-grained permission control
- Systems with frequently changing environments or business needs

When these challenges arise, many enterprises begin turning to the ABAC model. Although both RBAC and ABAC are used to protect system and data security, they differ significantly in permission assignment and management approaches.

## ABAC: A Powerful Yet Complex Access Control Model

ABAC offers finer-grained permission control than RBAC, making access decisions through multiple attributes, including:

- **User attributes**: such as department, security level, geographical location
- **Resource attributes**: such as classification level, owner, creation time
- **Operation attributes**: such as access time, previous operation records
- **Environment attributes**: such as device security, network location

This flexibility comes at a cost: compared to RBAC, ABAC is harder to understand and maintain. It requires more meticulous design and may be more complex during auditing and troubleshooting.

---

# ABAC vs. ReBAC: Conditional Judgment vs. Relationship Reasoning

Besides ABAC, another important alternative to RBAC is **ReBAC**. Both ABAC and ReBAC support fine-grained access control, but they solve problems in fundamentally different ways.

## ABAC's Advantageous Scenarios

ABAC is especially suitable for scenarios requiring complex logical judgments based on multiple attributes. For example, in a complex system, access permissions may depend on various factors such as time, location, and user status. Consider this example:

> "Only after midnight, and when the user is sitting at the bar counter, can visitors order specific drinks."

This policy combines time attributes, location attributes, and user attributes—exactly the typical application scenario for ABAC.

## Core Advantages of ReBAC

ReBAC leverages entity relationships within data models to simplify access control. Unlike ABAC, which relies on attribute tags, ReBAC makes permission judgments through associations between entities. Imagine a user named Jay who owns a locker containing various items. If using ABAC, you might need to tag each item with `Owner=Jay`. With ReBAC, you only need to define one rule:
> "Visitors can access all items within their own lockers."

The natural relationships formed between Jay, his locker, and the items within it constitute a clear access control structure.

ReBAC is particularly suitable for the following types of systems:

- Hierarchical structure systems
- Social networks
- Document management systems
- Team collaboration tools
- Any application with complex entity relationships

# Model Comparison: Practical Analysis

| Dimension | RBAC<br>(Role-Based Access Control) | ReBAC<br>(Relationship-Based Access Control) | ABAC<br>(Attribute-Based Access Control) |
|------|-------------------------------|----------------------------------|---------------------------------|
| **Conceptual Complexity** | Low | Medium to High | High |
| **Applicable Scenarios** | Role-based organizational structure permission management | Scenarios requiring access control based on entity relationships | Scenarios requiring dynamic permission judgment based on context |
| **Permission Granularity** | Coarse; prone to "role explosion" or overly broad permissions | Medium to High | High |
| **Typical Use Cases** | Sales personnel can view all sales data | Sales managers can view all sales data handled by their direct subordinates | Sales managers can only view sales data handled by their direct subordinates during working hours and via company computers |
| **Self-Development Implementation Difficulty** | Low to Medium | Medium to High | High |
| **Implementation Difficulty** | Low | Low to Medium | Low to Medium |

# Practical Application Scenario Analysis

In summary, different access control models are suitable for different types of application scenarios:

- **RBAC**: Suitable for enterprise-level applications with clear organizational structures
- **ABAC**: Suitable for systems requiring complex conditional judgments, such as finance and healthcare domains
- **ReBAC**: Suitable for social platforms, document management systems, and collaboration tools

# Hybrid Approach: Combining Advantages of Multiple Models
In practical development, many complex systems combine RBAC, ABAC, and ReBAC to meet permission control needs at different levels. Typical approaches include:

- Using **RBAC** to manage basic permissions
- Using **ABAC** to handle context-sensitive permissions
- Using **ReBAC** to manage relationship-based permissions

## Using RBAC as the Foundation Framework

RBAC is ideal for implementing coarse-grained access control. Its clear logic and ease of understanding make it friendly for both administrators and developers, often serving as the starting point for permission systems.

✅ **RBAC Example Scenario**: Analysts can view all reports

## Introducing ABAC to Enhance Flexibility

When permission control needs to consider dynamic factors such as time, location, and resource status, ABAC can be introduced on top of RBAC to enhance the adaptability of permission policies. For instance, a basic role might be granted permission to access financial reports, while ABAC can further restrict access to specific times, locations, or dynamically authorize based on report sensitivity levels.

✅ **RBAC + ABAC Example Scenario**: Analysts can only view reports during working hours

## Leveraging ReBAC to Manage Data Relationships

ReBAC excels at handling permissions based on data structures. Many systems overlay ReBAC on top of RBAC and ABAC to form more comprehensive permission control systems. For example, RBAC and ABAC can jointly define who can access which files, while ReBAC automatically identifies relationships like "who created this file" or "who belongs to a certain team" to determine access permissions.

✅ **RBAC + ReBAC Example Scenario**: Managers can access all files created by their team members

## Three-Way Combination: Addressing Most Complex Permission Needs
In some highly complex business scenarios, a single model can no longer meet requirements, necessitating the combined use of RBAC, ABAC, and ReBAC.

✅ **Three-Way Combination Example Scenario**: Doctors can only access patient medical records when they are in their assigned department, on duty, and have received patient authorization

# Key Considerations for Implementing Access Control

When implementing access control mechanisms, several key factors need to be comprehensively considered:

- **Performance Impact**: Complex permission checks may affect application response speed.
- **Development Experience**: Overly complex models can slow down development progress.
- **Maintenance Costs**: Permission rules need regular review and updates.
- **Scalability**: Your access control model should flexibly scale as your application evolves.

## Authorization-as-a-Service Model

Many enterprises attempting to implement ABAC or ReBAC themselves often need to embed substantial custom logic in their services, manually synchronize role or relationship data, and repeatedly write permission logic across multiple interfaces. For this reason, an increasing number of organizations are adopting "Authorization-as-a-Service" solutions to simplify permission management.

This architecture separates authorization logic from application code, bringing the following advantages:

- **Centralized Policy Management**
- **Consistent Control Across Services**
- **Stronger Audit Capabilities**
- **Lower Development Complexity**

An ideal application can combine multiple access control models: simply declaratively defining roles, attributes, and relationships in one place, and the application automatically handles policy execution, testing, and propagation.

# Choosing the Right Solution for Your Application
Selecting the most suitable access control model depends on your specific business needs:

- **Start from Data Model**: Understand entities in your system and their relationships
- **Identify Access Patterns**: How do users interact with resources?
- **Consider Future Development**: Will your access control needs become more complex over time?
- **Evaluate Implementation Resources**: More complex models typically mean higher development investment

## Decision Support Question Checklist

Different access control models vary in conceptual complexity: RBAC is easiest to understand and implement, ABAC introduces conditional logic, while ReBAC models complex relationships between users and resources. You can consider the following questions to assist decision-making:

- Are roles in your organization clear and relatively stable?
- Does permission judgment depend on dynamic factors such as time, location, or resource status?
- Are relationships between entities a core part of your data model?
- Do you need to support permission delegation or inheritance?

In traditional self-developed systems, these questions directly impact development and maintenance costs.

In an ideal application, these concerns should be decoupled. You still need to carefully consider which models best suit your application scenarios—but even the most complex models (like ABAC) become much simpler to implement. The program should provide a complete set of tools allowing you to uniformly, testably, and sustainably model relationships, conditions, and roles.

# Conclusion

Choosing among RBAC, ABAC, and ReBAC isn't about finding the "best" model, but about finding the one most suitable for your application scenario. Many successful systems adopt combinations of multiple models, leveraging their respective advantages in different scenarios.

As application systems become increasingly complex and interconnected, the industry is shifting toward more advanced access control models like ABAC and ReBAC. They not only provide the fine-grained control required by modern applications but also effectively address the limitations of traditional RBAC. Regardless of which model you ultimately choose, remember: access control is not only a core component of security design but also an important aspect of user experience. Taking the time to design a reasonable permission system will continue to yield returns throughout your application's lifecycle.