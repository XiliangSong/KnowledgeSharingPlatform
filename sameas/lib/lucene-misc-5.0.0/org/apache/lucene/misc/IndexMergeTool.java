package org.apache.lucene.misc;

/*
*
* Copyright(c) 2015, Samsung Electronics Co., Ltd.
* All rights reserved.
* 
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
  * Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
  * Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
  * Neither the name of the <organization> nor the
    names of its contributors may be used to endorse or promote products
    derived from this software without specific prior written permission.
  
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
* ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/ 

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Merges indices specified on the command line into the index
 * specified as the first command line argument.
 */
public class IndexMergeTool {
  public static void main(String[] args) throws IOException {
    if (args.length < 3) {
      System.err.println("Usage: IndexMergeTool <mergedIndex> <index1> <index2> [index3] ...");
      System.exit(1);
    }
    FSDirectory mergedIndex = FSDirectory.open(Paths.get(args[0]));

    IndexWriter writer = new IndexWriter(mergedIndex, new IndexWriterConfig(null)
        .setOpenMode(OpenMode.CREATE));

    Directory[] indexes = new Directory[args.length - 1];
    for (int i = 1; i < args.length; i++) {
      indexes[i  - 1] = FSDirectory.open(Paths.get(args[i]));
    }

    System.out.println("Merging...");
    writer.addIndexes(indexes);

    System.out.println("Full merge...");
    writer.forceMerge(1);
    writer.close();
    System.out.println("Done.");
  }
}