package cn.tz.chenjia.entity;

import cn.tz.chenjia.configs.ConfigsUtils;
import cn.tz.chenjia.rule.ESymbol;
import cn.tz.chenjia.utils.FileRWUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Help {

    private List<HelpInfo> helps;

    private static Help help;

    private Help(){}

    public static Help getInstance(){
        if(help == null){
            help = new Help();
            help.setHelps(help.readConf());
        }
        return help;
    }

    private List<HelpInfo> readConf(){
        List<HelpInfo> helps = new ArrayList<HelpInfo>();
        String helpsStr = FileRWUtils.read(ConfigsUtils.getSingleConf("help"));
        JSONArray ja = JSONObject.parseArray(helpsStr);
        for(int i=0 ; i<ja.size(); i++){
            JSONObject jo = ja.getJSONObject(i);
            HelpInfo info = new HelpInfo();
            info.setCode(jo.getString("code"));
            info.setResume(jo.getString("resume"));
            info.setDescription(jo.getString("description"));
            helps.add(info);
        }
        return helps;
    }

    public List<HelpInfo> getHelps() {
        return helps;
    }

    public void setHelps(List<HelpInfo> helps) {
        this.helps = helps;
    }

    public String findSingleHelp(String cmd){
        String r = "";
        for(HelpInfo info : helps){
            if(info.getCode().equalsIgnoreCase(cmd.trim())){
                r = info.getDescription();
                break;
            }
        }
        return r;
    }

    public String findAllHelp(){
        String r = "";
        for(HelpInfo info : helps){
            String code = info.getCode();
            r +=  info.getResume() + "\n" +code +"\n" + info.getDescription() + "\n\n"  + ESymbol.BORDER +"\n";
        }
        return r;
    }
}
class HelpInfo{
    private String code;

    private String resume;

    private String description;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}