package io.geeny.seeny.Util;


import io.geeny.seeny.Model.Tool.Config;
import io.geeny.seeny.Model.Elements.SubElements.Item;
import io.geeny.prettyprint.PrettyPrint;
import io.geeny.prettyprint.util.PrettyPrintConst;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by marcobierbach on 15.05.17.
 */
public class HelpMethods {

    private static HelpMethods instance;

    private PrettyPrint printer;
    private Map<String, String> memoryQueue = new HashMap<>();
    private String currentTestName;


    public HelpMethods(){
        instance = this;
    }

    /**
     * Helper method to read the content of a file from the resources websites folder and return the content as a string
     * @param _fileName
     * @return
     */
    public String getFileContentFromResourceFolder(String _folder, String _fileName) {

        StringBuilder result = new StringBuilder("");

        try (Scanner scanner = new Scanner(getFileInputStream(_folder+"/"+_fileName+ SeenyConst.FILE_ENDING))) {
            if(scanner != null) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    result.append(line).append("\n");
                }
            } else {
                getPrinter().parseText(String.format(Message.M_0044, _fileName, _folder), PrettyPrintConst.COLOR.ANSI_RED);
            }
        } catch (IOException e) {
            getPrinter().parseText(String.format(Message.M_0045, _fileName), PrettyPrintConst.COLOR.ANSI_RED);
        } catch (NullPointerException npe){
            getPrinter().parseText(String.format(Message.M_0046, _fileName, _folder),PrettyPrintConst.COLOR.ANSI_RED);
        }

        return result.toString();
    }

    /**
     * Method to initialize the printer from pretty print
     * @param _printType
     */
    public void initPrinter (PrettyPrintConst.PRINT_TYPE _printType){

        int width = Optional.ofNullable(Config.getInstance().getPrettyPrintValues().get("width"))
                .orElse(80);
        int padding = Optional.ofNullable(Config.getInstance().getPrettyPrintValues().get("padding"))
                .orElse(1);

        printer = new PrettyPrint(width, padding , _printType, getJarPath()+"/logs/");
    }

    /**
     * Helper method to read the content of a file from the websites folder and return the content as a string
     * @return
     */
    public String getFileContentFromLocalFolder(String _folder, String _fileName) {

        StringBuilder result = new StringBuilder("");

        String fileEnding = ".json";
        try(BufferedReader br = new BufferedReader(new FileReader(getJarPath()+"/"+_folder+"/"+_fileName+fileEnding))) {
            if(br != null) {
                String line = br.readLine();
                while (line != null) {
                    result.append(line);
                    result.append(System.lineSeparator());
                    line = br.readLine();
                }
            } else {
                getPrinter().parseText(String.format(Message.M_0047, _fileName, _folder), PrettyPrintConst.COLOR.ANSI_RED);
            }
        } catch (FileNotFoundException e) {
            getPrinter().parseText(String.format(Message.M_0047, _fileName, _folder), PrettyPrintConst.COLOR.ANSI_RED);
        } catch (IOException e) {
            getPrinter().parseText(String.format(Message.M_0045, _fileName), PrettyPrintConst.COLOR.ANSI_RED);
        }


        return result.toString();
    }

    /**
     * Method to check if the folder exists next to the jar file
     * @param _folder
     * @return
     */
    public boolean folderExists(String _folder){
        Path folder = Paths.get(getJarPath()+"/"+_folder);
        return Files.exists(folder);
    }

    /**
     * Method to load a file from a folder, the method will look first for the folder locally, next to the jar file
     * if this folder does not exists, it will be checked if it exists inside the resource folder of the jar file
     * @param _folder
     * @param _fileName
     * @return
     */
    public String getFileFromFolder(String _folder, String _fileName){
        String fileContent;
        if(folderExists(_folder)) {
            fileContent = getFileContentFromLocalFolder(_folder,_fileName);

            if(fileContent.isEmpty() == false) {
                printer.parseText(String.format(Message.M_0048, _fileName, _folder), PrettyPrintConst.COLOR.ANSI_GREEN);
                return fileContent;
            }
        }

        fileContent = getFileContentFromResourceFolder(_folder,_fileName);

        if(fileContent.isEmpty() == false) {
            printer.parseText(String.format(Message.M_0032, _fileName, _folder), PrettyPrintConst.COLOR.ANSI_GREEN);
        }

        return fileContent;
    }

    public InputStream getFileInputStream(String fileName) throws IOException{
        return getClass().getClassLoader().getResourceAsStream(fileName);
    }

    /**
     * Method to get a file from the resource folder
     *
     * @param _fileName
     * @return
     */
    public File getFile(String _fileName, String _fileEnding){
        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(_fileName + _fileEnding).getFile());
    }


    /**
     * Method to get a file from the resource folder
     *
     * @param _fileName
     * @return
     */
    public File getFileByTempFolder(File _tempfolder, String _fileName, String _fileEnding) throws IOException {
        //Get file from resources folder
        File dir = _tempfolder;
        File file = null;
        try (InputStream inputStream = getFileInputStream(_fileName+_fileEnding)){

            Path path = Paths.get(_fileName+_fileEnding);
            String pathString = path.getParent().toString();
            if(pathString.length()>0) {
                dir = new File(_tempfolder, pathString);
                dir.mkdirs();
                dir.deleteOnExit();
            }

            file = new File(dir, path.getFileName().toString());
            file.createNewFile();
            file.setExecutable(true);

            try (OutputStream out = new FileOutputStream(file)) {
                int read;
                byte[] bytes = new byte[1024];

                while ((read = inputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                out.flush();
            }
            file.deleteOnExit();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return file;
    }

    /**
     * Method to get the folder path where the jar file was executed
     * @return
     * @throws URISyntaxException
     */
    public String getJarPath() {
        CodeSource codeSource = HelpMethods.class.getProtectionDomain().getCodeSource();
        File jarFile = null;
        try {
            jarFile = new File(codeSource.getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        String jarDir = jarFile.getParentFile().getPath();
        return jarDir;
    }

    /**
     * Method to check the content for the items we look for
     * @param _content
     * @return
     */
    public String checkContent(String _content, String _regexp){
        Pattern pattern = Pattern.compile(_regexp);
        Matcher matcher = pattern.matcher(_content);
        while (matcher.find()) {
            return matcher.group(1); //return the found group
        }

        return "";
    }

    /** Method to handle the result of the item regexp based on the task of the item
     * @param _resultMatch
     * @param _item
     * @return
     */
    public boolean handleResult(String _resultMatch, Item _item){
        try {
            switch (_item.getTask()) {
                case FIND:
                    if (_resultMatch.isEmpty() == false) {
                        //the resultMatch is not empty, the group was found
                        HelpMethods.getInstance().getMemoryQueue().put(_item.getKey(), _resultMatch);
                        HelpMethods.getInstance().getPrinter().parseText(
                            String.format(Message.M_0033, _resultMatch, _item.getKey()));
                        return true;
                    }
                    break;
                case COMPARE:
                    //check if the result matches the expected value
                    if (_resultMatch.equals(_item.getValue())) {
                        return true;
                    }
                    break;
            }
        } catch (NullPointerException npe){
            npe.printStackTrace();
        }

        return false;
    }

    /**
     * Method for creation of randomly generated alphanumeric strings
     *
     * @param _length
     * @param _type
     * @return
     */
    public String generateRandomString(Enums.RANDOM_TYPE _type, int _length) {
        switch(_type) {
            case ALPHABETIC:
                return RandomStringUtils.randomAlphabetic(_length);
            case NUMERIC:
                return RandomStringUtils.randomNumeric(_length);
            case ALPHANUMERIC:
                return RandomStringUtils.randomAlphanumeric(_length);
            case ASCII:
            default:
                return RandomStringUtils.randomAscii(_length);
        }
    }

    /**
     *Method that extracts extractWildcard from the content and splits it into parameters for further use. Keycard follows "[string_int]" model
     *
     * @param _content
     * @return
     */
    public Object[] extractWildcard(String _content){
        //this is a randomizer wildcard
        String keycard = _content.substring(_content.indexOf(SeenyConst.RAND_SYMBOL)+2,_content.indexOf("]"));
        String type = keycard.substring(0,keycard.indexOf("_"));
        String length = keycard.substring(keycard.indexOf("_")+1);
        Object[] result = {type, length};
        return result;
    }

    public Enums.RANDOM_TYPE getRandomTypeByString(String _wildcard){
        for(Enums.RANDOM_TYPE type : Enums.RANDOM_TYPE.values()) {
            if(type.getKey().equals(_wildcard)) {
                return type;
            }
        }
        return Enums.RANDOM_TYPE.ASCII; //we will also give the ascii type as a default value to maximize the security
    }

    /**
     * Method to resolve the wildcards used in the content of an actor element
     * @param _content
     * @return
     */
    public String resolveWildCards(String _content){ return resolveWildCards(_content, null );}
    public String resolveWildCards(String _content, String _memoryName){
        if(_content.contains(SeenyConst.RAND_SYMBOL)) {
            //this is a randomizer wildcard
            Object[] wildCard = extractWildcard(_content);
            Enums.RANDOM_TYPE randomType = getRandomTypeByString((String)wildCard[0]);
            String randomString = generateRandomString(randomType,Integer.parseInt(wildCard[1].toString()));
            _content = _content.replaceAll(SeenyConst.RAND_PATTERN, randomString);

            if(_memoryName != null) {
                memorizeContent(_content, _memoryName);
            }
        }

        if(_content.contains(SeenyConst.MEMORY_SYMBOL)) {
            //this is a memoryWildcard
            _content = replaceWildCard(_content);
            getPrinter().parseText(String.format(Message.M_0034, _content));
        }

        return _content;
    }

    /**
     * Method to save the content to the memory map
     * @param _content
     * @param _memoryName
     */
    public void memorizeContent(String _content, String _memoryName){
        memoryQueue.put(_memoryName, _content);
        printer.parseText(String.format(Message.M_0057, _content, _memoryName));
    }

    /**
     * Method to replace the wildcard in a string by the value in the memory queue
     * @param _content
     * @return
     */
    public String replaceWildCard(String _content){
        Pattern p = Pattern.compile(SeenyConst.MEMORY_PATTERN);
        Matcher m = p.matcher(_content);
        String wildCard = SeenyConst.EMPTY ,part = SeenyConst.EMPTY;
        try {
            if(m.find()) {
                wildCard = m.group(0);
                part = m.group(1);
            }
            return _content.replace(wildCard, HelpMethods.getInstance().getMemoryQueue().get(part));
        } catch (Exception e){
            e.printStackTrace();
        }
        return _content;
    }

    public static HelpMethods getInstance() {
        return instance;
    }

    public PrettyPrint getPrinter() {
        return printer;
    }

    public Map<String, String> getMemoryQueue() {
        return memoryQueue;
    }

    public String getCurrentTestName() {
        return currentTestName;
    }

    public void setCurrentTestName(String currentTestName) {
        this.currentTestName = currentTestName;
    }
}
