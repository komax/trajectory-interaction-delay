function includeJavaLibaries()
% Only include custom java classes to class path
% if necessary
if isempty(javaclasspath('-dynamic'))
    %javaaddpath bin;
    javaaddpath build/classes;
end
end
