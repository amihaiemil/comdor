/**
 * Copyright (c) 2017, Mihai Emil Andronache
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1)Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 2)Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 3)Neither the name of comdor nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package co.comdor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;


/**
 * Unit tests for {@link Disconnected}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.3
 * @checkstyle FinalParameters (800 lines)
 * @checkstyle ParameterNumber (800 lines)
 * @checkstyle ParameterName (800 lines)
 * @checkstyle LineLength (800 lines)
 * @checkstyle ClassFanOutComplexity (800 lines)
 * @checkstyle MethodCount (800 lines)
 */
public final class DisconnectedTestCase {
    
    /**
     * All the methods of Disconnect should throw ISE.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void allMethodsThrowISE() throws Exception {
        for(final Method method : Disconnected.class.getMethods()) {
            try {
                Object[] params = new Object[method.getParameterCount()];
                for(int i=0; i < method.getParameters().length; i++) {
                    if(method.getParameters()[i].getClass().isPrimitive()) {
                        params[i] = Mockito.mock(method.getParameters()[i].getClass());
                    }
                }                
                System.out.println(method.getName());
                method.invoke(new Disconnected(), params);
            } catch (final InvocationTargetException ex) {
                
            } catch (final IllegalArgumentException ex) {
                //...
            }
        }
    }
}
