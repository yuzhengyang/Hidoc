# git pull
cd /home/hidev/hidoc/javadoc/gits/hicmp-utils/
git pull

cd /home/hidev/hidoc/javadoc/gits/hicmp/
git pull

cd /home/hidev/hidoc/javadoc/gits/cmp7zsh/
git pull


# do job
cd /home/hidev/hidoc/javadoc/


# update javadoc
sh job.sh name=hicmp-common-hiutils path=/home/hidev/hidoc/javadoc/gits/hicmp-utils/hicmp-common-hiutils token=40fc6acabd0a43e49df58923bb2abaf4

sh job.sh name=hicmp-feign-api path=/home/hidev/hidoc/javadoc/gits/hicmp-utils/hicmp-feign-api token=368546afa7f4486688c81713a5589e77

sh job.sh name=hicmp-sale path=/home/hidev/hidoc/javadoc/gits/hicmp/hicmp-sale token=685bdb4a65c44c4fbcda24375f45b3c4

sh job.sh name=hicmp-report path=/home/hidev/hidoc/javadoc/gits/hicmp/hicmp-report token=939995799d864343b5df9e0511c825ed
