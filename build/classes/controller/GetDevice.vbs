Set objWMIService = GetObject("winmgmts:\\.\root\cimv2")
 
Set colItems = objWMIService.ExecQuery _
("Select * from Win32_ComputerSystemProduct")
 
For Each objItem in colItems 
 
    Wscript.Echo objItem.Name
    Wscript.Echo objItem.UUID
 
Next
