/**
 * Copyright (c) 2017-2018, Mihai Emil Andronache
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
package co.comdor.github;

import java.io.IOException;
import java.util.Random;
import java.util.function.Consumer;

import co.comdor.IntermediaryStep;
import co.comdor.Knowledge;
import co.comdor.Log;
import co.comdor.Step;
import co.comdor.Steps;

import com.jcabi.github.Labels;

/**
 * The bot can create the Github Labels specified in .comdor.yml.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 0.0.3
 */
public final class CreateLabels implements Knowledge {

    /**
     * What do we do if it's not a 'labels' command?
     */
    private Knowledge notLabels;

    /**
     * Ctor.
     * @param notLabels What do we do if it's not a 'labels' command?
     */
    public CreateLabels(final Knowledge notLabels) {
        this.notLabels = notLabels;
    }

    @Override
    public Steps start(
        final Command mention, final Log log
    ) throws IOException {
        final Steps resolved;
        if("labels".equalsIgnoreCase(mention.type())) {
            resolved =  new GithubSteps(
                new ArchitectCheck(
                    new Create(
                        new SendReply(
                            String.format(
                                mention.language().response(
                                    "labels.comment.successful"
                                ),
                                mention.author(),
                                log.location()
                            )
                        )
                    ),
                    new CommanderCheck(
                        new Create(
                            new SendReply(
                                String.format(
                                    mention.language().response(
                                        "labels.comment.successful"
                                    ),
                                    mention.author(),
                                    log.location()
                                )
                            )
                        ),
                        new SendReply(
                            String.format(
                                mention.language().response(
                                    "author.no.rights"
                                ),
                                mention.author()
                            )
                        )
                    )
                ),
                mention
            );
        } else {
            resolved = this.notLabels.start(mention, log);
        }
        return resolved;
    }

    /**
     * Step where the labels are created.
     * @author Mihai Andronache (amihaiemil@gmail.com)
     * @version $Id$
     * @since 0.0.3
     * @checkstyle AnonInnerLength (100 lines)
     */
    static final class Create extends IntermediaryStep {

        /**
         * Ctor.
         * @param next The next step to perform.
         */
        Create(final Step next) {
            super(next);
        }

        @Override
        public void perform(
            final Command command, final Log log
        ) throws IOException {
            log.logger().info("Creating labels...");
            final Labels.Smart repo = new Labels.Smart(
                command.issue().repo().labels()
            );
            command.comdorYaml().labels().forEach(
                new Consumer<String>() {
                    private final Random colors = new Random();

                    @Override
                    public void accept(final String label) {
                        if(!repo.contains(label)) {
                            try {
                                repo.create(label, this.pickRandom(colors));
                            } catch (final IOException ex) {
                                throw new IllegalStateException(
                                    "IOException when creating labels", ex
                                );
                            }
                        }
                    }
                    
                    /**
                     * Pick a random hex RGB color.
                     * @param random Randomizer.
                     * @return String hex color (without the leading '#').
                     * @checkstyle MagicNumber (50 lines)
                     */
                    private String pickRandom(final Random random) {
                        final String[] values = {
                            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                            "a", "b", "c", "d", "e", "f"
                        };
                        final StringBuilder color = new StringBuilder();
                        for (int idx = 0; idx < 6; idx++) {
                            color.append(values[random.nextInt(values.length)]);
                        }
                        return color.toString();
                    }
                }
            );
            log.logger().info("Labels successfully created!");
            this.next().perform(command, log);
        }
    }
}