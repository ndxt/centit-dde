# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Centit-DDE** (Data Develop Engine) is a data processing and exchange platform built on Spring MVC + Dubbo. It provides a configurable pipeline engine where data flows through a chain of `BizOperation` steps defined in `DataPacket` configurations.

## Build Commands

```bash
# Build all modules
mvn clean install

# Build without tests
mvn clean package -DskipTests

# Build a specific module
mvn clean package -pl centit-dde-core -DskipTests

# Run all tests
mvn test

# Run tests in a specific module
mvn test -pl centit-dde-core

# Run a single test class
mvn test -pl centit-dde-core -Dtest=TestSqlite
```

Java 8 target, Maven 3.x required.

## Module Structure

| Module | Purpose |
|--------|---------|
| `centit-dde-adapter` | DAO layer, PO entities (DataPacket, CallApiLog, etc.) |
| `centit-dde-core` | Core engine — 61 `BizOperation` implementations in `com.centit.dde.bizopt` |
| `centit-dde-module` | Spring REST controllers (`DataPacketController`, `DoApiController`, etc.) |
| `centit-dde-console` | WAR web console, Dubbo client config, Spring MVC wiring |
| `centit-dde-rmdb` | Relational database operations |
| `centit-dde-json` | JSON data transformations |
| `centit-dde-datamoving` | Standalone Spring Boot data movement service |
| `centit-dde-*-plugin` | Optional plugins: Kafka, ES, FTP, Redis, Workflow, WebServices, WebCrawler, OFD, DynamicCodes |

## Core Architecture

### Data Flow

```
DataPacket (JSON config stored in DB)
  → BizOptFlowImpl.run()
  → BizModel (data container)
  → BizOperation[] (sequential processing steps)
  → DataOptResult (output)
```

### Key Abstractions

**`BizOperation`** — functional interface, the unit of work:
```java
ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext)
```
All 61 implementations live in `centit-dde-core/src/main/java/com/centit/dde/bizopt/`.

**`BizModel`** — data container passed through the pipeline:
- `bizData`: `Map<String, DataSet>` — named datasets
- `stackData`: `Map<String, Object>` — internal/temp params (prefixed `__`)
- `optResult`: accumulated operation results

**`DataSet`** — virtual data collection wrapping a `Collection`, `Map`, or scalar. Types: `T` (table), `R` (row), `S` (scalar), `E` (empty).

**`DataOptContext`** — execution context carrying request params, headers, session, logging (`CallApiLog`), and run mode (`DEBUG` / `NORMAL`).

**`BizOptFlow`** / `BizOptFlowImpl`** — orchestrates the operation chain; registers all `BizOperation` beans and drives execution.

### Adding a New BizOperation

1. Create a class in `centit-dde-core/.../bizopt/` implementing `BizOperation`.
2. Register it as a Spring bean (or register manually in `BizOptFlowImpl`).
3. The operation receives `bizOptJson` (its config slice from the DataPacket) and reads/writes datasets on `BizModel`.

### Deployment

- **Console** (`centit-dde-console`): WAR deployed to a servlet container; uses Dubbo for RPC and Nacos for config (`system.properties`).
- **DataMoving** (`centit-dde-datamoving`): standalone Spring Boot JAR (`DataMovingApplication`).

### Configuration

- Nacos / local properties: `centit-dde-console/src/main/resources/system.properties`
- Dubbo server: `centit-dde-console/src/main/resources/dubbo-dde-server.xml`
- Logging: `log4j2.xml`

  centit-dde 项目结构
  这是一个 数据开发引擎（Data Develop Engine），基于 Spring MVC + Dubbo 构建，核心思想是：把数据处理逻辑拆成一个个可配置的"操作节点"，串联成流水线执行。
                                                                                                                                                                                                                                    
  ---                                                   
  分层架构

  centit-dde-console        ← WAR包，Web控制台（Dubbo客户端）
  centit-dde-module         ← REST控制器层（HTTP接口）
  centit-dde-core           ← 核心引擎（61个BizOperation实现）
  centit-dde-adapter        ← 数据访问层（DAO、PO实体）
  centit-dde-rmdb           ← 关系型数据库操作
  centit-dde-json           ← JSON数据处理
  centit-dde-agent          ← 独立的Spring Boot数据迁移服务
  centit-dde-*-plugin       ← 可选插件（Kafka、ES、FTP、Redis等）

  ---
  核心执行流程

  DataPacket（数据库中存的JSON配置）
  ↓
  BizOptFlowImpl.run()      ← 流程编排器
  ↓
  BizModel（数据容器）       ← 贯穿整个流水线
  ↓
  BizOperation[]            ← 每个节点依次处理
  ↓
  DataOptResult（输出结果）

  ---
  四个核心抽象

  ┌────────────────┬──────────────────────────────────────────────┐
  │       类       │                     作用                     │
  ├────────────────┼──────────────────────────────────────────────┤
  │ BizOperation   │ 函数式接口，每个处理节点的契约               │
  ├────────────────┼──────────────────────────────────────────────┤
  │ BizModel       │ 流水线的数据容器，持有所有 DataSet           │
  ├────────────────┼──────────────────────────────────────────────┤
  │ DataSet        │ 虚拟数据集，可以是表/行/标量/空              │
  ├────────────────┼──────────────────────────────────────────────┤
  │ DataOptContext │ 执行上下文，含请求参数、会话、日志、运行模式 │
  └────────────────┴──────────────────────────────────────────────┘

  ---
  插件体系

  centit-dde-core 里有 61 个 BizOperation 实现，覆盖：
    - 数据库读写（RMDB）
    - HTTP调用（CallAPI）
    - 文件处理（上传/下载/FTP）
    - 加解密、格式转换
    - 工作流集成
    - ES/Redis/Kafka 等中间件

  每个插件模块独立打包，按需引入。

  ---
  部署形态

    - centit-dde-console → 部署到 Tomcat，通过 Nacos 注册，Dubbo 做 RPC
    - centit-dde-agent → 独立 Spring Boot JAR，直接运行

