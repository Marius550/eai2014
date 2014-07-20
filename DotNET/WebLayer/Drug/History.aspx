<%@ Page Title="" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true" CodeBehind="History.aspx.cs" Inherits="WebLayer.Drug.History" %>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentHolder" runat="server">
    <form id="form1" runat="server">
        <asp:LinqDataSource ID="InventoryHistoryDataSource" runat="server" ContextTypeName="Pharmacy.BusinessLayer.Data.PharmacyContainer" EntityTypeName="" Select="new (DateOfAction, Quantity, Drug)" TableName="InventoryEventSet" Where="DrugPZN == @DrugPZN">
            <WhereParameters>
                <asp:QueryStringParameter Name="DrugPZN" QueryStringField="PZN" Type="Int32" />
            </WhereParameters>
        </asp:LinqDataSource>
        <asp:GridView ID="HistoryGridView" DataSourceID="InventoryHistoryDataSource" runat="server" AutoGenerateColumns="False">
            <Columns>
                <asp:BoundField DataField="DateOfAction" HeaderText="Date" SortExpression="DateOfAction" />
                <asp:BoundField DataField="Quantity" HeaderText="Quantity" SortExpression="Quantity" />
            </Columns>
        </asp:GridView>
    </form>
</asp:Content>
