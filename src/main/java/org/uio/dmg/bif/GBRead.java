/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uio.dmg.bif;


import com.sun.org.apache.xalan.internal.utils.FeatureManager.Feature;
import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.List;



import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava.nbio.core.sequence.compound.DNACompoundSet;
 
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.features.FeatureInterface;
import org.biojava.nbio.core.sequence.io.DNASequenceCreator;
import org.biojava.nbio.core.sequence.io.GenbankReader;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.biojava.nbio.core.sequence.io.GenericGenbankHeaderParser;
import org.biojava.nbio.core.sequence.io.ProteinSequenceCreator;
import org.biojava.nbio.core.sequence.loader.GenbankProxySequenceReader;
import org.biojava.nbio.core.sequence.template.AbstractSequence;



/**
 *
 * @author simonray
 */

public class GBRead {
 
public static void main(String[] args) throws Exception{
	/*
	 * Method 1: With the GenbankProxySequenceReader
	 */
	//Try with the GenbankProxySequenceReader
	GenbankProxySequenceReader<AminoAcidCompound> genbankProteinReader 
	= new GenbankProxySequenceReader<AminoAcidCompound>("/tmp", "NP_000257", AminoAcidCompoundSet.getAminoAcidCompoundSet());
	ProteinSequence proteinSequence = new ProteinSequence(genbankProteinReader);
	genbankProteinReader.getHeaderParser().parseHeader(genbankProteinReader.getHeader(), proteinSequence);
	System.out.println("Sequence" + "(" + proteinSequence.getAccession() + "," + proteinSequence.getLength() + ")=" +
proteinSequence.getSequenceAsString().substring(0, 10) + "...");
 
	GenbankProxySequenceReader<NucleotideCompound> genbankDNAReader 
	= new GenbankProxySequenceReader<NucleotideCompound>("/tmp", "NM_001126", DNACompoundSet.getDNACompoundSet());
	DNASequence dnaSequence = new DNASequence(genbankDNAReader);
	genbankDNAReader.getHeaderParser().parseHeader(genbankDNAReader.getHeader(), dnaSequence);
	System.out.println("Sequence" + "(" + dnaSequence.getAccession() + "," + dnaSequence.getLength() + ")=" +
dnaSequence.getSequenceAsString().substring(0, 10) + "...");
	/*
	 * Method 2: With the GenbankReaderHelper
	 */
	//Try with the GenbankReaderHelper
	File dnaFile = new File("src/test/resources/NM_000266.gb");		
	File protFile = new File("src/test/resources/BondFeature.gb");
 
	LinkedHashMap<String, DNASequence> dnaSequences = GenbankReaderHelper.readGenbankDNASequence( dnaFile );
	for (DNASequence sequence : dnaSequences.values()) {
	    	System.out.println( sequence.getSequenceAsString() );
                List<FeatureInterface<AbstractSequence<NucleotideCompound>,NucleotideCompound>> fl = sequence.getFeatures();
                for (FeatureInterface f : fl) {
                    System.out.println(f.getType());
                    System.out.println(f.getQualifiers());
                }
	}
 
	LinkedHashMap<String, ProteinSequence> protSequences = GenbankReaderHelper.readGenbankProteinSequence(protFile);
	for (ProteinSequence sequence : protSequences.values()) {
		System.out.println( sequence.getSequenceAsString() );
	}
	/*
	 * Method 3: With the GenbankReader Object 
	 */		
	//Try reading with the GanbankReader
	FileInputStream is = new FileInputStream(dnaFile);
	GenbankReader<DNASequence, NucleotideCompound> dnaReader = new GenbankReader<DNASequence, NucleotideCompound>(
	        is, 
	        new GenericGenbankHeaderParser<DNASequence,NucleotideCompound>(),
	        new DNASequenceCreator(DNACompoundSet.getDNACompoundSet())
	);
	dnaSequences = dnaReader.process();
	is.close();
	System.out.println(dnaSequences);
 
	is = new FileInputStream(protFile);
	GenbankReader<ProteinSequence, AminoAcidCompound> protReader = new GenbankReader<ProteinSequence, AminoAcidCompound>(
	        is,
	        new GenericGenbankHeaderParser<ProteinSequence,AminoAcidCompound>(),
	        new ProteinSequenceCreator(AminoAcidCompoundSet.getAminoAcidCompoundSet())
	);
	protSequences = protReader.process();
	is.close();
	System.out.println(protSequences);
    } 
}
