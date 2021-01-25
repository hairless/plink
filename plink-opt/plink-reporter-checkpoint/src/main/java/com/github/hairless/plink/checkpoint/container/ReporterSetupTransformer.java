package com.github.hairless.plink.checkpoint.container;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author lipan
 * @date 2021-01-24
 */
public class ReporterSetupTransformer implements ClassFileTransformer {
    private static final String clazz = "org.apache.flink.runtime.metrics.ReporterSetup";
    private static final String method = "fromConfiguration";
    private static final String code = "com.github.hairless.plink.checkpoint.container.ConfigurationHolder.init(configuration);";

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (!className.replace("/", ".").equals(clazz))
            return null;
        System.out.println("start transform class..." + clazz);
        try {
            CtClass ctclass = ClassPool.getDefault().get(clazz);
            CtMethod executeMethod = ctclass.getDeclaredMethod(method);
            executeMethod.insertBefore(code);
            return ctclass.toBytecode();
        } catch (Exception e) {
            System.err.println("transform class " + clazz + " fail..." + e.getMessage());
        }
        return null;
    }
}
