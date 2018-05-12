package fr.bzh.rzh.fec.model;

import java.util.function.Supplier;
import java.util.stream.Stream;

import fr.bzh.rzh.fec.IFecObjectStream;

/**
 * 
 * @author A667119 KHERBICHE LYES
 *
 */
@SuppressWarnings("serial")
public class FecObjectStream extends FecObject implements IFecObjectStream {
	
	private Supplier<Stream<String>> streamSupplier;

	/*
	 * TODO implement this method && check if streamSupplier&Stream<String
	 *      are not null
	 */
	public Supplier<Stream<String>> getStreamSupplier() {
		return streamSupplier;
	}

	public void setStreamSupplier(Supplier<Stream<String>> streamSupplier) {
		this.streamSupplier = streamSupplier;
	}

	@Override
	public String toString() {
		return super.toString()+
				" \nstreamSupplier: "+streamSupplier.toString();
	}

	@Override
	public boolean equals(Object o) {
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public Stream<String> getFileStream() {
		return streamSupplier.get();
	}
	
	
}
