
-- --------------------------------------------------
-- Entity Designer DDL Script for SQL Server 2005, 2008, 2012 and Azure
-- --------------------------------------------------
-- Date Created: 06/17/2014 20:23:18
-- Generated from EDMX file: C:\Users\Tobi\Documents\GitHub\group-04\DotNET\BusinessLayer\Data\Pharmacy.edmx
-- --------------------------------------------------

SET QUOTED_IDENTIFIER OFF;
GO
USE [Pharmacy04];
GO
IF SCHEMA_ID(N'dbo') IS NULL EXECUTE(N'CREATE SCHEMA [dbo]');
GO

-- --------------------------------------------------
-- Dropping existing FOREIGN KEY constraints
-- --------------------------------------------------

IF OBJECT_ID(N'[dbo].[FK_DrugInventoryEvent]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[InventoryEventSet] DROP CONSTRAINT [FK_DrugInventoryEvent];
GO
IF OBJECT_ID(N'[dbo].[FK_DrugPosition]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[PositionSet] DROP CONSTRAINT [FK_DrugPosition];
GO
IF OBJECT_ID(N'[dbo].[FK_ReplenishmentOrderPosition]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[PositionSet] DROP CONSTRAINT [FK_ReplenishmentOrderPosition];
GO
IF OBJECT_ID(N'[dbo].[FK_ItemPrescribedDrug]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[ItemSet] DROP CONSTRAINT [FK_ItemPrescribedDrug];
GO
IF OBJECT_ID(N'[dbo].[FK_PrescriptionItem]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[ItemSet] DROP CONSTRAINT [FK_PrescriptionItem];
GO
IF OBJECT_ID(N'[dbo].[FK_CustomerPrescription]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[PrescriptionSet] DROP CONSTRAINT [FK_CustomerPrescription];
GO
IF OBJECT_ID(N'[dbo].[FK_WithdrawEvent_inherits_InventoryEvent]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[InventoryEventSet_WithdrawEvent] DROP CONSTRAINT [FK_WithdrawEvent_inherits_InventoryEvent];
GO
IF OBJECT_ID(N'[dbo].[FK_RestockEvent_inherits_InventoryEvent]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[InventoryEventSet_RestockEvent] DROP CONSTRAINT [FK_RestockEvent_inherits_InventoryEvent];
GO
IF OBJECT_ID(N'[dbo].[FK_ReplenishEvent_inherits_InventoryEvent]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[InventoryEventSet_ReplenishEvent] DROP CONSTRAINT [FK_ReplenishEvent_inherits_InventoryEvent];
GO

-- --------------------------------------------------
-- Dropping existing tables
-- --------------------------------------------------

IF OBJECT_ID(N'[dbo].[DrugSet]', 'U') IS NOT NULL
    DROP TABLE [dbo].[DrugSet];
GO
IF OBJECT_ID(N'[dbo].[InventoryEventSet]', 'U') IS NOT NULL
    DROP TABLE [dbo].[InventoryEventSet];
GO
IF OBJECT_ID(N'[dbo].[PositionSet]', 'U') IS NOT NULL
    DROP TABLE [dbo].[PositionSet];
GO
IF OBJECT_ID(N'[dbo].[ReplenishmentOrderSet]', 'U') IS NOT NULL
    DROP TABLE [dbo].[ReplenishmentOrderSet];
GO
IF OBJECT_ID(N'[dbo].[PrescriptionSet]', 'U') IS NOT NULL
    DROP TABLE [dbo].[PrescriptionSet];
GO
IF OBJECT_ID(N'[dbo].[ItemSet]', 'U') IS NOT NULL
    DROP TABLE [dbo].[ItemSet];
GO
IF OBJECT_ID(N'[dbo].[CustomerSet]', 'U') IS NOT NULL
    DROP TABLE [dbo].[CustomerSet];
GO
IF OBJECT_ID(N'[dbo].[InventoryEventSet_WithdrawEvent]', 'U') IS NOT NULL
    DROP TABLE [dbo].[InventoryEventSet_WithdrawEvent];
GO
IF OBJECT_ID(N'[dbo].[InventoryEventSet_RestockEvent]', 'U') IS NOT NULL
    DROP TABLE [dbo].[InventoryEventSet_RestockEvent];
GO
IF OBJECT_ID(N'[dbo].[InventoryEventSet_ReplenishEvent]', 'U') IS NOT NULL
    DROP TABLE [dbo].[InventoryEventSet_ReplenishEvent];
GO

-- --------------------------------------------------
-- Creating all tables
-- --------------------------------------------------

-- Creating table 'DrugSet'
CREATE TABLE [dbo].[DrugSet] (
    [PZN] int  NOT NULL,
    [Name] nvarchar(max)  NOT NULL,
    [Description] nvarchar(max)  NULL,
    [Stock] int  NOT NULL,
    [MinimumInventoryLevel] int  NOT NULL,
    [OptimalInventoryLevel] int  NOT NULL
);
GO

-- Creating table 'InventoryEventSet'
CREATE TABLE [dbo].[InventoryEventSet] (
    [Id] int IDENTITY(1,1) NOT NULL,
    [Quantity] int  NOT NULL,
    [DateOfAction] datetime  NOT NULL,
    [DrugPZN] int  NOT NULL
);
GO

-- Creating table 'PositionSet'
CREATE TABLE [dbo].[PositionSet] (
    [Id] int IDENTITY(1,1) NOT NULL,
    [Quantity] int  NOT NULL,
    [DrugPZN] int  NOT NULL,
    [ReplenishmentOrderId] int  NOT NULL
);
GO

-- Creating table 'ReplenishmentOrderSet'
CREATE TABLE [dbo].[ReplenishmentOrderSet] (
    [Id] int IDENTITY(1,1) NOT NULL,
    [State] int  NOT NULL,
    [ExpectedDelivery] datetime  NULL,
    [ActualDelivery] datetime  NULL
);
GO

-- Creating table 'PrescriptionSet'
CREATE TABLE [dbo].[PrescriptionSet] (
    [Id] int IDENTITY(1,1) NOT NULL,
    [State] int  NOT NULL,
    [IssuingPhysician] nvarchar(max)  NOT NULL,
    [IssueDate] datetime  NOT NULL,
    [EntryDate] datetime  NOT NULL,
    [FulfilmentDate] datetime  NULL,
    [CustomerId] int  NOT NULL
);
GO

-- Creating table 'ItemSet'
CREATE TABLE [dbo].[ItemSet] (
    [Id] int IDENTITY(1,1) NOT NULL,
    [State] int  NOT NULL,
    [DrugPZN] int  NOT NULL,
    [PrescriptionId] int  NOT NULL
);
GO

-- Creating table 'CustomerSet'
CREATE TABLE [dbo].[CustomerSet] (
    [Id] int IDENTITY(1,1) NOT NULL,
    [Name] nvarchar(max)  NOT NULL,
    [TelephoneNumber] nvarchar(max)  NOT NULL,
    [Address] nvarchar(max)  NULL
);
GO

-- Creating table 'InventoryEventSet_WithdrawEvent'
CREATE TABLE [dbo].[InventoryEventSet_WithdrawEvent] (
    [Id] int  NOT NULL
);
GO

-- Creating table 'InventoryEventSet_RestockEvent'
CREATE TABLE [dbo].[InventoryEventSet_RestockEvent] (
    [Id] int  NOT NULL
);
GO

-- Creating table 'InventoryEventSet_ReplenishEvent'
CREATE TABLE [dbo].[InventoryEventSet_ReplenishEvent] (
    [Id] int  NOT NULL
);
GO

-- --------------------------------------------------
-- Creating all PRIMARY KEY constraints
-- --------------------------------------------------

-- Creating primary key on [PZN] in table 'DrugSet'
ALTER TABLE [dbo].[DrugSet]
ADD CONSTRAINT [PK_DrugSet]
    PRIMARY KEY CLUSTERED ([PZN] ASC);
GO

-- Creating primary key on [Id] in table 'InventoryEventSet'
ALTER TABLE [dbo].[InventoryEventSet]
ADD CONSTRAINT [PK_InventoryEventSet]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'PositionSet'
ALTER TABLE [dbo].[PositionSet]
ADD CONSTRAINT [PK_PositionSet]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'ReplenishmentOrderSet'
ALTER TABLE [dbo].[ReplenishmentOrderSet]
ADD CONSTRAINT [PK_ReplenishmentOrderSet]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'PrescriptionSet'
ALTER TABLE [dbo].[PrescriptionSet]
ADD CONSTRAINT [PK_PrescriptionSet]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'ItemSet'
ALTER TABLE [dbo].[ItemSet]
ADD CONSTRAINT [PK_ItemSet]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'CustomerSet'
ALTER TABLE [dbo].[CustomerSet]
ADD CONSTRAINT [PK_CustomerSet]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'InventoryEventSet_WithdrawEvent'
ALTER TABLE [dbo].[InventoryEventSet_WithdrawEvent]
ADD CONSTRAINT [PK_InventoryEventSet_WithdrawEvent]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'InventoryEventSet_RestockEvent'
ALTER TABLE [dbo].[InventoryEventSet_RestockEvent]
ADD CONSTRAINT [PK_InventoryEventSet_RestockEvent]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- Creating primary key on [Id] in table 'InventoryEventSet_ReplenishEvent'
ALTER TABLE [dbo].[InventoryEventSet_ReplenishEvent]
ADD CONSTRAINT [PK_InventoryEventSet_ReplenishEvent]
    PRIMARY KEY CLUSTERED ([Id] ASC);
GO

-- --------------------------------------------------
-- Creating all FOREIGN KEY constraints
-- --------------------------------------------------

-- Creating foreign key on [DrugPZN] in table 'InventoryEventSet'
ALTER TABLE [dbo].[InventoryEventSet]
ADD CONSTRAINT [FK_DrugInventoryEvent]
    FOREIGN KEY ([DrugPZN])
    REFERENCES [dbo].[DrugSet]
        ([PZN])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_DrugInventoryEvent'
CREATE INDEX [IX_FK_DrugInventoryEvent]
ON [dbo].[InventoryEventSet]
    ([DrugPZN]);
GO

-- Creating foreign key on [DrugPZN] in table 'PositionSet'
ALTER TABLE [dbo].[PositionSet]
ADD CONSTRAINT [FK_DrugPosition]
    FOREIGN KEY ([DrugPZN])
    REFERENCES [dbo].[DrugSet]
        ([PZN])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_DrugPosition'
CREATE INDEX [IX_FK_DrugPosition]
ON [dbo].[PositionSet]
    ([DrugPZN]);
GO

-- Creating foreign key on [ReplenishmentOrderId] in table 'PositionSet'
ALTER TABLE [dbo].[PositionSet]
ADD CONSTRAINT [FK_ReplenishmentOrderPosition]
    FOREIGN KEY ([ReplenishmentOrderId])
    REFERENCES [dbo].[ReplenishmentOrderSet]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_ReplenishmentOrderPosition'
CREATE INDEX [IX_FK_ReplenishmentOrderPosition]
ON [dbo].[PositionSet]
    ([ReplenishmentOrderId]);
GO

-- Creating foreign key on [DrugPZN] in table 'ItemSet'
ALTER TABLE [dbo].[ItemSet]
ADD CONSTRAINT [FK_ItemPrescribedDrug]
    FOREIGN KEY ([DrugPZN])
    REFERENCES [dbo].[DrugSet]
        ([PZN])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_ItemPrescribedDrug'
CREATE INDEX [IX_FK_ItemPrescribedDrug]
ON [dbo].[ItemSet]
    ([DrugPZN]);
GO

-- Creating foreign key on [PrescriptionId] in table 'ItemSet'
ALTER TABLE [dbo].[ItemSet]
ADD CONSTRAINT [FK_PrescriptionItem]
    FOREIGN KEY ([PrescriptionId])
    REFERENCES [dbo].[PrescriptionSet]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_PrescriptionItem'
CREATE INDEX [IX_FK_PrescriptionItem]
ON [dbo].[ItemSet]
    ([PrescriptionId]);
GO

-- Creating foreign key on [CustomerId] in table 'PrescriptionSet'
ALTER TABLE [dbo].[PrescriptionSet]
ADD CONSTRAINT [FK_CustomerPrescription]
    FOREIGN KEY ([CustomerId])
    REFERENCES [dbo].[CustomerSet]
        ([Id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_CustomerPrescription'
CREATE INDEX [IX_FK_CustomerPrescription]
ON [dbo].[PrescriptionSet]
    ([CustomerId]);
GO

-- Creating foreign key on [Id] in table 'InventoryEventSet_WithdrawEvent'
ALTER TABLE [dbo].[InventoryEventSet_WithdrawEvent]
ADD CONSTRAINT [FK_WithdrawEvent_inherits_InventoryEvent]
    FOREIGN KEY ([Id])
    REFERENCES [dbo].[InventoryEventSet]
        ([Id])
    ON DELETE CASCADE ON UPDATE NO ACTION;
GO

-- Creating foreign key on [Id] in table 'InventoryEventSet_RestockEvent'
ALTER TABLE [dbo].[InventoryEventSet_RestockEvent]
ADD CONSTRAINT [FK_RestockEvent_inherits_InventoryEvent]
    FOREIGN KEY ([Id])
    REFERENCES [dbo].[InventoryEventSet]
        ([Id])
    ON DELETE CASCADE ON UPDATE NO ACTION;
GO

-- Creating foreign key on [Id] in table 'InventoryEventSet_ReplenishEvent'
ALTER TABLE [dbo].[InventoryEventSet_ReplenishEvent]
ADD CONSTRAINT [FK_ReplenishEvent_inherits_InventoryEvent]
    FOREIGN KEY ([Id])
    REFERENCES [dbo].[InventoryEventSet]
        ([Id])
    ON DELETE CASCADE ON UPDATE NO ACTION;
GO

-- --------------------------------------------------
-- Script has ended
-- --------------------------------------------------