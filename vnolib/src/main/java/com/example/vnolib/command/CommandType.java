package com.example.vnolib.command;


import com.example.vnolib.command.ascommands.COCommand;
import com.example.vnolib.command.ascommands.CVCommand;
import com.example.vnolib.command.ascommands.NoCommand;
import com.example.vnolib.command.ascommands.RPSCommand;
import com.example.vnolib.command.ascommands.SDPCommand;
import com.example.vnolib.command.ascommands.VNALCommand;
import com.example.vnolib.command.servercommands.ADCommand;
import com.example.vnolib.command.servercommands.ARCCommand;
import com.example.vnolib.command.servercommands.AllowedCommand;
import com.example.vnolib.command.servercommands.CADCommand;
import com.example.vnolib.command.servercommands.CTCommand;
import com.example.vnolib.command.servercommands.ChangeCommand;
import com.example.vnolib.command.servercommands.FORCESTREAMResponseCommand;
import com.example.vnolib.command.servercommands.GaBResponseCommand;
import com.example.vnolib.command.servercommands.GmBResponseCommand;
import com.example.vnolib.command.servercommands.LISTCommand;
import com.example.vnolib.command.servercommands.MCCommand;
import com.example.vnolib.command.servercommands.MODCommand;
import com.example.vnolib.command.servercommands.MODOKCommand;
import com.example.vnolib.command.servercommands.MSCommand;
import com.example.vnolib.command.servercommands.PCCommand;
import com.example.vnolib.command.servercommands.POPUPCommand;
import com.example.vnolib.command.servercommands.RADCommand;
import com.example.vnolib.command.servercommands.RCDCommand;
import com.example.vnolib.command.servercommands.MDCommand;
import com.example.vnolib.command.servercommands.RMDCommand;
import com.example.vnolib.command.servercommands.ROOKCommand;
import com.example.vnolib.command.servercommands.ReqCommand;
import com.example.vnolib.command.servercommands.RoCCommand;
import com.example.vnolib.command.servercommands.SERVURLCommand;
import com.example.vnolib.command.servercommands.SHOWURLCommand;
import com.example.vnolib.command.servercommands.TKNCommand;

public enum CommandType {

    CV(CVCommand.class),
    CO(COCommand.class),
    VNAL(VNALCommand.class),
    RPS(RPSCommand.class),
    SDP(SDPCommand.class),
    No(NoCommand.class),
    PC(PCCommand.class),
    MOD(MODCommand.class),
    MODOK(MODOKCommand.class),
    GaB(GaBResponseCommand.class),
    GmB(GmBResponseCommand.class),
    RCD(RCDCommand.class),
    CAD(CADCommand.class),
    RMD(RMDCommand.class),
    MD(MDCommand.class),
    RAD(RADCommand.class),
    AD(ADCommand.class),
    Req(ReqCommand.class),
    Allowed(AllowedCommand.class),
    TKN(TKNCommand.class),
    Change(ChangeCommand.class),
    MS(MSCommand.class),
    MC(MCCommand.class),
    FORCESTREAM(FORCESTREAMResponseCommand.class),
    SHOWURL(SHOWURLCommand.class),
    SERVURL(SERVURLCommand.class),
    ARC(ARCCommand.class),
    ROOK(ROOKCommand.class),
    RoC(RoCCommand.class),
    LIST(LISTCommand.class),
    POPUP(POPUPCommand.class),
    CT(CTCommand.class);

    CommandType(Class<? extends BaseCommand> clazz) {
        this.commandClass = clazz;
    }


    public final Class<? extends BaseCommand> commandClass;
}
