package uk.ac.ebi.pride.jmztab;

import org.apache.commons.cli.*;
import uk.ac.ebi.pride.jmztab.utils.errors.MZTabErrorList;
import uk.ac.ebi.pride.jmztab.utils.errors.MZTabErrorType;
import uk.ac.ebi.pride.jmztab.utils.errors.MZTabErrorTypeMap;
import uk.ac.ebi.pride.jmztab.model.MZTabFile;
import uk.ac.ebi.pride.jmztab.utils.*;
import uk.ac.ebi.pride.jmztab.utils.convert.ConvertFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * User: qingwei
 * Date: 27/02/13
 */
public class MZTabCommandLine {
    public static void main(String[] args) throws Exception {
        MZTabErrorTypeMap typeMap = new MZTabErrorTypeMap();

        // Definite command line
        CommandLineParser parser = new PosixParser();
        Options options = new Options();

        String helpOpt = "help";
        options.addOption("h", helpOpt, false, "print help message");

        String msgOpt = "message";
        String codeOpt = "code";
        Option msgOption = OptionBuilder.withArgName(codeOpt)
                .hasArgs(2)
                .withValueSeparator()
                .withDescription("print Error/Warn detail message based on code number.")
                .create(msgOpt);
        options.addOption(msgOption);

        String inDirOpt = "inDir";
        options.addOption(inDirOpt, true, "Setting input file directory. If not set, default input " +
                                          "directory is current application path.");

        String outDirOpt = "outDir";
        options.addOption(outDirOpt, true, "Setting output file directory. If not set, default output " +
                                           " directory is same with input directory.");

        String outOpt = "outFile";
        options.addOption(outOpt, true, "Record error/warn messages into outfile. If not set, print message on the screen. ");

        String checkOpt = "check";
        String inFileOpt = "inFile";
        Option checkOption = OptionBuilder.withArgName(inFileOpt)
                .hasArgs(2)
                .withValueSeparator()
                .withDescription("Choose a file from input directory. This parameter should not be null!")
                .create(checkOpt);
        options.addOption(checkOption);

        String convertOpt = "convert";
        String formatOpt = "format";
        Option convertOption = OptionBuilder.withArgName(inFileOpt + ", " + formatOpt)
                .hasArgs()
                .withValueSeparator()
                .withDescription("Converts the given format file to an mztab file.")
                .create(convertOpt);
        options.addOption(convertOption);

        String mergeOpt = "merge";
        String inFileListOpt = "inFiles";
        String combineOpt = "combine";
        Option mergeOption = OptionBuilder.withArgName(inFileListOpt + ", " + combineOpt)
                .hasArgs()
                .withValueSeparator()
                .withDescription("Merge multiple comma-delimited mztab files into one File.")
                .create(mergeOpt);
        options.addOption(mergeOption);


        // Parse command line
        CommandLine line = parser.parse(options, args);
        if (line.hasOption(helpOpt)) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("jmztab", options);
        } else if (line.hasOption(msgOpt)) {
            String[] values = line.getOptionValues(msgOpt);
            Integer code = new Integer(values[1]);
            MZTabErrorType type = typeMap.getType(code);

            if (type == null) {
                System.out.println("Not found MZTabErrorType, the code is :" + code);
            } else {
                System.out.println(type);
            }
        } else {
            File inDir = null;
            if (line.hasOption(inDirOpt)) {
                inDir = new File(line.getOptionValue(inDirOpt));
                if (! inDir.isDirectory()) {
                    throw new IllegalArgumentException("input file directory not setting!");
                }
            }
            if (inDir == null) {
                inDir = new File(".");
            }

            // if not provide output directory, default use input directory.
            File outDir = null;
            if (line.hasOption(outDirOpt)) {
                outDir = new File(line.getOptionValue(outDirOpt));
            }
            if (outDir == null || !outDir.isDirectory()) {
                outDir = inDir;
            }

            File outFile = null;
            if (line.hasOption(outOpt)) {
                outFile = new File(outDir, line.getOptionValue(outOpt));
            }
            OutputStream out = outFile == null ? System.out : new BufferedOutputStream(new FileOutputStream(outFile));

            if (line.hasOption(checkOpt)) {
                String[] values = line.getOptionValues(checkOpt);
                if (values.length != 2) {
                    throw new IllegalArgumentException("Not setting input file!");
                }
                File inFile = new File(inDir, values[1].trim());
                System.out.println("Begin check mztab file: " + inFile.getAbsolutePath());
                new MZTabFileParser(inFile, out);
            } else if (line.hasOption(convertOpt)) {
                String[] values = line.getOptionValues(convertOpt);
                File inFile = null;
                String format = null;
                for (int i = 0; i < values.length; i++) {
                    String type = values[i++].trim();
                    String value = values[i].trim();
                    if (type.equals(inFileOpt)) {
                        inFile = new File(inDir, value.trim());
                    } else if (type.equals(formatOpt)) {
                        format = value.trim();
                    }
                }
                if (inFile == null) {
                    throw new IllegalArgumentException("Not setting input file!");
                }
                if (format == null) {
                    format = ConvertFile.PRIDE;
                }

                System.out.println("Begin convert " + inFile.getAbsolutePath() + " which format is " + format + " to mztab file.");
                MZTabFileConverter converter = new MZTabFileConverter(inFile, format);
                MZTabFile tabFile = converter.getMZTabFile();
                MZTabErrorList errorList = new MZTabErrorList();
                MZTabFileChecker checker = new MZTabFileChecker(errorList);
                checker.check(tabFile, MZTabProperties.LEVEL);
                if (errorList.isEmpty()) {
                    System.out.println("Begin print mztab file.");
                    tabFile.printMZTab(out);
                } else {
                    System.out.println("There exists some errors in mztab file.");
                    errorList.print(out);
                }
            } else if (line.hasOption(mergeOpt)) {
                String[] values = line.getOptionValues(mergeOpt);
                List<File> inFileList = new ArrayList<File>();
                boolean combine = false;
                for (int i = 0; i < values.length; i++) {
                    String type = values[i++].trim();
                    String value = values[i].trim();
                    if (type.equals(inFileListOpt)) {
                        String[] fileNames = value.split(",");
                        for (String fileName : fileNames) {
                            inFileList.add(new File(inDir, fileName));
                        }
                    } else if (type.equals(combineOpt)) {
                        combine = value.equals("true");
                    }
                }

                MZTabFileParser mzParser;
                MZTabFileMerger merger = new MZTabFileMerger();
                System.out.println("Begin merge multiple mztab files into one.");
                for (File inFile : inFileList) {
                    System.out.println("Begin load mztab file " + inFile.getAbsolutePath());
                    mzParser = new MZTabFileParser(inFile, out);
                    merger.addTabFile(mzParser.getMZTabFile());
                }

                merger.setCombine(combine);
                System.out.println("Begin merge mztab files.");
                MZTabFile tabFile = merger.merge();
                MZTabErrorList errorList = new MZTabErrorList();
                System.out.println("Begin check merged mztab file.");
                MZTabFileChecker checker = new MZTabFileChecker(errorList);
                checker.check(tabFile, MZTabProperties.LEVEL);
                if (errorList.isEmpty()) {
                    System.out.println("Begin print merged mztab file.");
                    tabFile.printMZTab(out);
                } else {
                    System.out.println("There exists some errors in merged mztab files.");
                    errorList.print(out);
                }
            }

            System.out.println("Finish!");
            System.out.println();
            out.close();
        }
    }
}
