package xyz.udalny.vnolib.command;


import xyz.udalny.vnolib.command.ascommands.COCommand;
import xyz.udalny.vnolib.command.ascommands.CVCommand;
import xyz.udalny.vnolib.command.ascommands.NoCommand;
import xyz.udalny.vnolib.command.ascommands.RPSCommand;
import xyz.udalny.vnolib.command.ascommands.SDPCommand;
import xyz.udalny.vnolib.command.ascommands.VNALCommand;
import xyz.udalny.vnolib.command.servercommands.ADCommand;
import xyz.udalny.vnolib.command.servercommands.ARCCommand;
import xyz.udalny.vnolib.command.servercommands.AllowedCommand;
import xyz.udalny.vnolib.command.servercommands.CADCommand;
import xyz.udalny.vnolib.command.servercommands.CTCommand;
import xyz.udalny.vnolib.command.servercommands.ChangeCommand;
import xyz.udalny.vnolib.command.servercommands.FORCESTREAMResponseCommand;
import xyz.udalny.vnolib.command.servercommands.GaBResponseCommand;
import xyz.udalny.vnolib.command.servercommands.GmBResponseCommand;
import xyz.udalny.vnolib.command.servercommands.LISTCommand;
import xyz.udalny.vnolib.command.servercommands.MCCommand;
import xyz.udalny.vnolib.command.servercommands.MDCommand;
import xyz.udalny.vnolib.command.servercommands.MODCommand;
import xyz.udalny.vnolib.command.servercommands.MODOKCommand;
import xyz.udalny.vnolib.command.servercommands.MSCommand;
import xyz.udalny.vnolib.command.servercommands.PCCommand;
import xyz.udalny.vnolib.command.servercommands.POPUPCommand;
import xyz.udalny.vnolib.command.servercommands.RADCommand;
import xyz.udalny.vnolib.command.servercommands.RCDCommand;
import xyz.udalny.vnolib.command.servercommands.RMDCommand;
import xyz.udalny.vnolib.command.servercommands.ROOKCommand;
import xyz.udalny.vnolib.command.servercommands.RaCCommand;
import xyz.udalny.vnolib.command.servercommands.ReqCommand;
import xyz.udalny.vnolib.command.servercommands.RoCCommand;
import xyz.udalny.vnolib.command.servercommands.SERVURLCommand;
import xyz.udalny.vnolib.command.servercommands.SHOWURLCommand;
import xyz.udalny.vnolib.command.servercommands.TKNCommand;

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
    RaC(RaCCommand.class),
    RoC(RoCCommand.class),
    LIST(LISTCommand.class),
    POPUP(POPUPCommand.class),
    CT(CTCommand.class);

    CommandType(Class<? extends BaseCommand> clazz) {
        this.commandClass = clazz;
    }


    public final Class<? extends BaseCommand> commandClass;
}
