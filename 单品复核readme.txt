	@WarehouseID_R		varchar(35), --V422（仓库，传空值或*号）
	@CustomerID			Varchar(30),	（客户ID，固定值UEB）
	@WaveNo				varChar(20),		--V401v420（波次编号，订单所在的波次号）
	@SKU				Char(50), （SKU编号）
	@WorkStation        varchar(30), --V423      操作台（空值或空字符串）
	@Language			char(1),
	@UserID				Varchar(35),  登陆账号
	
	@TOID				Varchar(30)	output,
	@OrderNo			Varchar(20)	OutPut,
	
	
SPSO_SingleSKU_Match单品复核存储过程：
  1、调用GETSYS_configuration函数：得知@WarehouseID_R参数的含义。@WarehouseID    varchar(50),--仓库
  	调用者dbo.getsys_configuration(@WarehouseID_R, '*', '*', 'SGL_ORD_PCK', default, default)
  	
GETSYS_configuration函数参数说明如下：	
-- Add the parameters for the function here
@WarehouseID    varchar(50),--仓库 
@CustomerID     varchar(50),--货主 
@OrderType      varchar(50),--单据类型 
@ConfigID       varchar(50),--参数 
@DefaultValue   varchar(50)='N',
@ValueType      varchar(50)='C')

  		select @ResultValue = rtrim(VALUE_STRING)
		from sys_configuration 
		where WarehouseID=isnull(@WarehouseID,'*') and CustomerID=isnull(@CustomerID,'*') AND 
			OrderType=isnull(@OrderType,'*') AND CONFIG_ID=@ConfigID;
  
  	set @IDX_CHK_WGT = dbo.getsys_configuration(@WarehouseID_R, '*', '*', 'IDX_CHK_WGT','N','C') --V422C
    set @PKG_REU_TID = dbo.getsys_configuration(@WarehouseID_R, '*', '*', 'PKG_REU_TID','N','C') --V375 --V422C
    set @PKG_CFM_PCK = dbo.getsys_configuration(@WarehouseID_R, '*', '*', 'PKG_CFM_PCK','N','C') --V375 --V422C
    set @CAT_RLS_SRT = dbo.getsys_configuration(@WarehouseID_R, '*', '*', 'CAT_RLS_SRT','N','C') --V363 --V422C
    set @SN#_CTL = dbo.getsys_configuration(@WarehouseID_R, '*', '*', 'SN#_CTL', '0', 'C') --V417 --V422C
    
