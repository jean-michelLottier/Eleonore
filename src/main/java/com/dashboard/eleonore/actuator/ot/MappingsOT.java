package com.dashboard.eleonore.actuator.ot;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class MappingsOT extends EndPointOT {
    private Map<String, ContextOT> contexts;

    public MappingsOT() {
        super("mappings");
    }

    @Getter
    @Setter
    private static class ContextOT {
        private String parentId;
        private MappingOT mappings;

        @Getter
        @Setter
        private static class MappingOT {
            private ServletOT[] servlets;
            private ServletFilterOT[] servletFilters;
            private DispatcherServletsOT dispatcherServlets;
            private DispatcherHandlersOT dispatcherHandlers;

            @Getter
            @Setter
            private static class ServletOT {
                private String[] mappings;
                private String name;
                private String className;
            }

            @Getter
            @Setter
            private static class ServletFilterOT {
                private String[] servletNameMappings;
                private String[] urlPatternMappings;
                private String name;
                private String className;
            }

            @Getter
            @Setter
            private static class DispatcherServletsOT {
                private DispatcherServletOT[] dispatcherServlet;

                @Getter
                @Setter
                private static class DispatcherServletOT {
                    private String handler;
                    private String predicate;
                    private DetailsOT details;

                    @Getter
                    @Setter
                    private static class DetailsOT {
                        private HandlerMethodOT handlerMethod;
                        private RequestMappingConditionsOT requestMappingConditions;

                        @Getter
                        @Setter
                        private static class HandlerMethodOT {
                            private String className;
                            private String name;
                            private String descriptor;
                        }

                        @Getter
                        @Setter
                        private static class RequestMappingConditionsOT {
                            private ConsumeOT[] consumes;
                            private HeaderOT[] headers;
                            private String[] methods;
                            private HeaderOT[] params;
                            private String[] patterns;
                            private ConsumeOT[] produces;
                        }
                    }
                }
            }

            @Getter
            @Setter
            private static class DispatcherHandlersOT {
                private String handler;
                private String predicate;
                private DetailsOT[] details;

                @Getter
                @Setter
                private static class DetailsOT {
                    private RequestMappingConditionsOT requestMappingConditions;

                    @Getter
                    @Setter
                    private static class RequestMappingConditionsOT {
                        private ConsumeOT[] consumes;
                        private HeaderOT[] headers;
                        private String[] methods;
                        private HeaderOT[] params;
                        private ConsumeOT[] produces;
                        private HandlerMethodOT handlerMethod;

                        @Getter
                        @Setter
                        private static class HandlerMethodOT {
                            private String className;
                            private String name;
                            private String descriptor;
                            private HandlerFunctionOT handlerFunction;

                            @Getter
                            @Setter
                            private static class HandlerFunctionOT {
                                private String className;
                            }
                        }
                    }
                }
            }

            @Getter
            @Setter
            private static class ConsumeOT {
                private String mediaType;
                private boolean negated;

            }

            @Getter
            @Setter
            private static class HeaderOT {
                private String name;
                private String value;
                private boolean negated;
            }
        }
    }
}
